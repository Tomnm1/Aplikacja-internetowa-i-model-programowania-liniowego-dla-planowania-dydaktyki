package pl.poznan.put.or_planner.insert;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.poznan.put.planner_endpoints.GeneratedPlan.GeneratedPlan;
import pl.poznan.put.planner_endpoints.GeneratedPlan.GeneratedPlanService;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Plan.Plan;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.SlotsDay.Day;
import pl.poznan.put.planner_endpoints.SlotsDay.SlotsDay;
import pl.poznan.put.planner_endpoints.SlotsDay.SlotsDayService;
import pl.poznan.put.planner_endpoints.Subject.Subject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static pl.poznan.put.constans.Constans.FieldsOfStudyTypes.*;

@Service
public class PlanToExcelExportService {
    private final GeneratedPlanService generatedPlanService;
    private final SlotsDayService slotsDayService;
    Workbook workbook = new XSSFWorkbook();

    @Autowired
    PlanToExcelExportService(
            GeneratedPlanService generatedPlanService,
            SlotsDayService slotsDayService
    ){
        this.generatedPlanService = generatedPlanService;
        this.slotsDayService = slotsDayService;
    }

    @Transactional
    public void exportPlanToExcel(Plan plan) throws IOException {
        Random random = new Random();

        Map<Semester, List<GeneratedPlan>> semesterListMap = new HashMap<>();
        Map<Subject, XSSFCellStyle> subjectColors = new HashMap<>();

        List<GeneratedPlan> planObjects = generatedPlanService.getGeneratedPlansByPlanId(plan);
        String fieldOfStudyTyp = planObjects.get(0).group.semester.specialisation.fieldOfStudy.typ;

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
        }

        Map<SlotsDay, Integer> slots = processSlots(fieldOfStudyTyp);

        for (Semester semester: semesterListMap.keySet()){
            Sheet sheet = workbook.createSheet(semester.specialisation.fieldOfStudy.name + " " +
                    semester.specialisation.cycle + " " + semester.specialisation.name + " " + semester.number);
            processSheet(sheet, semesterListMap.get(semester), slots, subjectColors);
            mergeCellsIfEqual(sheet, 2, 2);
        }

        try (FileOutputStream fileOut = new FileOutputStream("Plan_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".xlsx")) {
            workbook.write(fileOut);
            System.out.println("Plan zapisany");
        } finally {
            workbook.close();
        }
    }

    private Map<SlotsDay, Integer> processSlots(String typ){
        int firstSlotRowIndex = 2;
        Map<SlotsDay, Integer> slots = new HashMap<>();
        List<Day> days = new ArrayList<>();
        if(Objects.equals(typ, FULL_TIME)){
            days = fullTimeDays;
        } else if(Objects.equals(typ, PART_TIME)){
            days = partTimeDays;
        }
        for(Day day: days){
            List<SlotsDay> slotsDays = slotsDayService.getAllSlotsDayForDay(day);
            for(SlotsDay slotsDay: slotsDays){
                slots.put(slotsDay, firstSlotRowIndex);
                firstSlotRowIndex += 2;
            }
        }
        return slots;
    }

    private void processSheet(Sheet sheet, List<GeneratedPlan> planObjects, Map<SlotsDay, Integer> slots,
                              Map<Subject, XSSFCellStyle> subjectColors){
        Row sheetNameRow = sheet.createRow(0);
        Row headerRow = sheet.createRow(1);
        headerRow.createCell(1).setCellValue("Grupa");

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
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            Cell cell = row.createCell(groups.get(planObject.group));

            String shortValue = String.format(
                    "%s %s %s %s",
                    planObject.classroom.code,
                    getInitials(planObject.subjectType.subject.name),
                    getInitials(planObject.teacher.firstName + " " + planObject.teacher.lastName),
                    getInitials(planObject.subjectType.type.toString())
            );
            cell.setCellValue(shortValue);
            cell.setCellStyle(cellStyle);

            CreationHelper factory = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = factory.createClientAnchor();
            anchor.setCol1(cell.getColumnIndex());
            anchor.setCol2(cell.getColumnIndex() + 2);
            anchor.setRow1(row.getRowNum());
            anchor.setRow2(row.getRowNum() + 2);

            Comment comment = drawing.createCellComment(anchor);
            RichTextString fullInfo = factory.createRichTextString(
                    String.format(
                            "%s %s %s %s %s",
                            planObject.classroom.code,
                            planObject.subjectType.subject.name,
                            planObject.teacher.firstName,
                            planObject.teacher.lastName,
                            planObject.subjectType.type
                    )
            );
            comment.setString(fullInfo);
            comment.setAuthor("Auto-Generated");
            cell.setCellComment(comment);

            sheet.autoSizeColumn(groups.get(planObject.group));
        }
    }

    private void prepareHeaderAndRows(Sheet sheet, Row headerRow, Map<Group, Integer> groups,
                                      Map<SlotsDay, Integer> slots, Row sheetNameRow){
        int firstGroupColumnIndex = 2;
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
                .sorted(Comparator.comparing(group -> this.extractNumberFromGroupCode(group.code)))
                .toList();
        for (Group group : sortedGroups) {
            groups.put(group, firstGroupColumnIndex++);
        }

        for(Group group: groups.keySet()){
            headerRow.createCell(groups.get(group)).setCellValue(group.code);
        }

        XSSFCellStyle dayCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        dayCellStyle.setRotation((short) 90);
        dayCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dayCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont boldFont = (XSSFFont) workbook.createFont();
        boldFont.setBold(true);
        dayCellStyle.setFont(boldFont);

        for (SlotsDay slotsDay : slots.keySet()) {
            int startRow = slots.get(slotsDay);
            int endRow = startRow + 1;

            Row row = sheet.createRow(startRow);
            row.createCell(1).setCellValue(slotsDay.slot.startTime + " P");

            Row nextRow = sheet.createRow(endRow);
            nextRow.createCell(1).setCellValue(slotsDay.slot.startTime + " N");

            Cell dayCell = row.createCell(0);
            dayCell.setCellValue(slotsDay.day.toString());
            dayCell.setCellStyle(dayCellStyle);

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
        }

        String previousDay = null;
        int mergeStartRow = -1;
        int currentRow = 0;

        while (currentRow <= sheet.getLastRowNum()) {
            Row row = sheet.getRow(currentRow);
            if (row == null) {
                currentRow++;
                continue;
            }
            Cell dayCell = row.getCell(0);
            if (dayCell == null) {
                currentRow++;
                continue;
            }

            String currentDay = dayCell.getStringCellValue();

            if (!currentDay.equals(previousDay)) {
                if (mergeStartRow != -1 && previousDay != null) {
                    sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, currentRow - 1, 0, 0));
                }
                previousDay = currentDay;
                mergeStartRow = currentRow;
            }

            currentRow++;
        }

        if (mergeStartRow != -1 && previousDay != null) {
            sheet.addMergedRegion(new CellRangeAddress(mergeStartRow, sheet.getLastRowNum(), 0, 0));
        }
    }

    public void mergeCellsIfEqual(Sheet sheet, int startRow, int startCol) {
        for (int row = startRow; row <= sheet.getLastRowNum(); row++) {
            Row currentRow = sheet.getRow(row);
            if (currentRow == null) continue;

            int mergeStartCol = startCol;
            String lastValue = null;

            for (int col = startCol; col <= currentRow.getLastCellNum(); col++) {
                Cell cell = currentRow.getCell(col);
                String cellValue = (cell != null) ? cell.getStringCellValue() : null;

                if (lastValue == null || !lastValue.equals(cellValue)) {
                    if (mergeStartCol < col - 1) {
                        addMergedRegionIfPossible(sheet, row, row, mergeStartCol, col - 1);
                    }
                    mergeStartCol = col;
                }

                lastValue = cellValue;
            }

            if (mergeStartCol < currentRow.getLastCellNum()) {
                addMergedRegionIfPossible(sheet, row, row, mergeStartCol, currentRow.getLastCellNum() - 1);
            }
        }

        for (int col = startCol; col <= sheet.getRow(startRow).getLastCellNum(); col++) {
            int mergeStartRow = startRow;
            String lastValue = null;

            for (int row = startRow; row <= sheet.getLastRowNum(); row++) {
                Row currentRow = sheet.getRow(row);
                if (currentRow == null) continue;

                Cell cell = currentRow.getCell(col);
                String cellValue = (cell != null) ? cell.getStringCellValue() : null;

                if (lastValue == null || !lastValue.equals(cellValue)) {
                    if (mergeStartRow < row - 1) {
                        addMergedRegionIfPossible(sheet, mergeStartRow, row - 1, col, col);
                    }
                    mergeStartRow = row;
                }
                lastValue = cellValue;
            }

            if (mergeStartRow < sheet.getLastRowNum()) {
                addMergedRegionIfPossible(sheet, mergeStartRow, sheet.getLastRowNum(), col, col);
            }
        }
    }

    private void addMergedRegionIfPossible(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        CellRangeAddress newRegion = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        for (CellRangeAddress existingRegion : sheet.getMergedRegions()) {
            if (existingRegion.intersects(newRegion)) {
                return;
            }
        }
        sheet.addMergedRegion(newRegion);
    }


    private String getInitials(String fullName) {
        StringBuilder initials = new StringBuilder();
        for (String part : fullName.split(" ")) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }

    private int extractNumberFromGroupCode(String code){
        String numberPart = code.replaceAll("\\D+", "");
        return numberPart.isEmpty() ? 0 : Integer.parseInt(numberPart);
    }
}
