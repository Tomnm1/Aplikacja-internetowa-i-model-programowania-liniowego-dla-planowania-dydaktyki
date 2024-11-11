package pl.poznan.put.or_planner.insert;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.poznan.put.planner_endpoints.GeneratedPlan.GeneratedPlan;
import pl.poznan.put.planner_endpoints.GeneratedPlan.GeneratedPlanService;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Plan.Plan;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.SlotsDay.SlotsDay;
import pl.poznan.put.planner_endpoints.Subject.Subject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PlanToExcelExportService {
    private final GeneratedPlanService generatedPlanService;

    @Autowired
    PlanToExcelExportService(
            GeneratedPlanService generatedPlanService
    ){
        this.generatedPlanService = generatedPlanService;
    }

    @Transactional
    public void exportPlanToExcel(Plan plan) throws IOException {
        Random random = new Random();

        int firstSlotRowIndex = 2;
        Workbook workbook = new XSSFWorkbook();

        Map<Semester, List<GeneratedPlan>> semesterListMap = new HashMap<>();
        Map<Subject, XSSFCellStyle> subjectColors = new HashMap<>();
        Map<SlotsDay, Integer> slots = new HashMap<>();

        List<GeneratedPlan> planObjects = generatedPlanService.getGeneratedPlansByPlanId(plan);

        for(GeneratedPlan planObject: planObjects){
            semesterListMap.computeIfAbsent(planObject.group.semester, k -> new ArrayList<>()).add(planObject);

            Subject subject = planObject.subjectType.subject;
            if (!subjectColors.containsKey(planObject.subjectType.subject)) {
                byte[] rgbColor = new byte[]{
                        (byte) (75 + random.nextInt(175)),
                        (byte) (75 + random.nextInt(175)),
                        (byte) (75 + random.nextInt(175))
                };
                XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
                cellStyle.setFillForegroundColor(new XSSFColor(rgbColor, null));
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                subjectColors.put(subject, cellStyle);
            }

            if(!slots.containsKey(planObject.slotsDay)){
                slots.put(planObject.slotsDay, firstSlotRowIndex);
                firstSlotRowIndex += 2;
            }
        }

        for (Semester semester: semesterListMap.keySet()){
            Sheet sheet = workbook.createSheet(semester.specialisation.fieldOfStudy.name + " " +
                    semester.specialisation.name + " " + semester.number);
            processSheet(sheet, semesterListMap.get(semester), slots, subjectColors);
        }

        try (FileOutputStream fileOut = new FileOutputStream("Plan_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".xlsx")) {
            workbook.write(fileOut);
            System.out.println("Plan zapisany");
        } finally {
            workbook.close();
        }
    }

    private void processSheet(Sheet sheet, List<GeneratedPlan> planObjects, Map<SlotsDay, Integer> slots,
                              Map<Subject, XSSFCellStyle> subjectColors){
        Row sheetNameRow = sheet.createRow(0);
        Row headerRow = sheet.createRow(1);
        headerRow.createCell(0).setCellValue("Grupa");

        Map<Group, Integer> groups = new HashMap<>();

        for(GeneratedPlan planObject: planObjects){
            groups.putIfAbsent(planObject.group, null);
        }

        prepareHeaderAndRows(sheet, headerRow, groups, slots, sheetNameRow);

        insertPlannedDataOnSheet(sheet, planObjects, slots, subjectColors, groups);
    }

    private void insertPlannedDataOnSheet(Sheet sheet, List<GeneratedPlan> planObjects, Map<SlotsDay, Integer> slots,
                                          Map<Subject, XSSFCellStyle> subjectColors, Map<Group, Integer> groups){
        for(GeneratedPlan planObject: planObjects){
            int rowIndex = slots.get(planObject.slotsDay);
            if(planObject.isEvenWeek) rowIndex++;
            Row row = sheet.getRow(rowIndex);
            XSSFCellStyle cellStyle = subjectColors.get(planObject.subjectType.subject);
            Cell cell = row.createCell(groups.get(planObject.group));
            cell.setCellValue(
                    planObject.classroom.code + " " + planObject.subjectType.subject.name + " " +
                            planObject.teacher.firstName + " " + planObject.teacher.lastName
            );
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(groups.get(planObject.group));
        }
    }

    private void prepareHeaderAndRows(Sheet sheet, Row headerRow, Map<Group, Integer> groups,
                                      Map<SlotsDay, Integer> slots, Row sheetNameRow){
        int firstGroupColumnIndex = 1;
        int lastGroupColumnIndex = firstGroupColumnIndex + groups.size();

        Cell cell = sheetNameRow.createCell(firstGroupColumnIndex);
        cell.setCellValue(sheet.getSheetName());

        sheet.addMergedRegion(new CellRangeAddress(
                sheetNameRow.getRowNum(),
                sheetNameRow.getRowNum(),
                firstGroupColumnIndex,
                lastGroupColumnIndex
        ));

        List<Group> sortedGroups = groups.keySet()
                .stream()
                .sorted(Comparator.comparing(group -> group.code))
                .toList();
        for (Group group : sortedGroups) {
            groups.put(group, firstGroupColumnIndex++);
        }

        for(Group group: groups.keySet()){
            headerRow.createCell(groups.get(group)).setCellValue(group.code);
        }
        for(SlotsDay slotsDay: slots.keySet()){
            Row row = sheet.createRow(slots.get(slotsDay));
            row.createCell(0).setCellValue(slotsDay.day.toString() + " " + slotsDay.slot.startTime + " P");
            row = sheet.createRow(slots.get(slotsDay) + 1);
            row.createCell(0).setCellValue(slotsDay.day.toString() + " " + slotsDay.slot.startTime + " N");
            sheet.autoSizeColumn(0);
        }

    }
}
