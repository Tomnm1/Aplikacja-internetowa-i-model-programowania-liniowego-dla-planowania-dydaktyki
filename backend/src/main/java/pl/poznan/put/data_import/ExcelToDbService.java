package pl.poznan.put.data_import;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.data_import.insert_to_db.FieldOfStudyHandler;
import pl.poznan.put.data_import.insert_to_db.SemesterHandler;
import pl.poznan.put.data_import.insert_to_db.SpecialisationHandler;
import pl.poznan.put.data_import.insert_to_db.SubjectHandler;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;
import pl.poznan.put.planner_endpoints.Subject.Language;
import pl.poznan.put.planner_endpoints.Subject.Subject;

import java.util.*;
import java.util.function.Consumer;

import static pl.poznan.put.constans.Constans.ExcelToDb.ColumnNames.*;
import static pl.poznan.put.constans.Constans.ExcelToDb.HeaderHelper.*;
import static pl.poznan.put.constans.Constans.ExcelToDb.HeaderHelper.Prefixes.*;

@Service
public class ExcelToDbService {
    private Sheet sheet;
    private Row row;

    private final Map<String, Integer> columnIndices = new HashMap<>();
    private final Map<String, Consumer<Cell>> columnActions = new HashMap<>();

    private final FieldOfStudyHandler fieldOfStudyHandler;
    private final SpecialisationHandler specialisationHandler;
    private final SemesterHandler semesterHandler;
    private final SubjectHandler subjectHandler;

    private String cycle = "";
    private String semesterNumber = "";
    private String type = ""; //stacjo - niestacjo
    private boolean exam;
    private String subjectName = "";

    private FieldOfStudy fieldOfStudy;
    private String fieldOfStudyName;
    private Specialisation specialisation;
    private String specialisationName;
    private Semester semester;
    private Subject subject;

    @Autowired
    public ExcelToDbService(
        FieldOfStudyHandler fieldOfStudyHandler,
        SpecialisationHandler specialisationHandler,
        SemesterHandler semesterHandler,
        SubjectHandler subjectHandler
    ){
        this.fieldOfStudyHandler = fieldOfStudyHandler;
        this.specialisationHandler = specialisationHandler;
        this.semesterHandler = semesterHandler;
        this.subjectHandler = subjectHandler;
    }
    public void startSheetProcessing(Sheet sheet){
        Row headerRow = sheet.getRow(2);
        Row headerRowHelper = sheet.getRow(3);
        processHeaderRow(headerRow, headerRowHelper);

        prepareColumnActions();

        processAllRows(sheet, 5);

    }

    private void insertData(){
        this.fieldOfStudy = assignIfNotNull(fieldOfStudyHandler.insertFieldOfStudy(fieldOfStudyName), this.fieldOfStudy);
        this.specialisation = assignIfNotNull(specialisationHandler.insertSpecialisation(specialisationName, cycle, fieldOfStudy), this.specialisation);
        this.semester = assignIfNotNull(semesterHandler.insertSemester(semesterNumber, specialisation), this.semester);
        this.subject = assignIfNotNull(subjectHandler.insertSubject(subjectName, exam, false, false, Language.polski, semester), this.subject);
    }

    private void processAllRows(Sheet sheet, int startingRow){
        this.sheet = sheet;
        for (int i = startingRow; i < sheet.getLastRowNum(); i++){
            row = sheet.getRow(i);
            for (Map.Entry<String, Consumer<Cell>> entry : columnActions.entrySet()) {
                String columnName = entry.getKey();
                Consumer<Cell> action = entry.getValue();
                int columnIndex = columnIndices.get(columnName);
                Cell cell = row.getCell(columnIndex);
                if(cell != null) {
                    action.accept(cell);
                }
            }
            insertData();
        }
    }

    private void prepareColumnActions(){
        columnActions.put(FIELD_OF_STUDY, this::processFieldOfStudy);
        columnActions.put(TYPE_FACULTY, this::processTypeFaculty);
        columnActions.put(TERM, this::processTerm);
        columnActions.put(SUBJECT, this::processSubject);
    }

    private void processSubject(Cell cell){
        String cellValue = cell.getStringCellValue();
        if(!cellValue.isEmpty()) {
            subjectName = cellValue;
            String examValue = row.getCell(columnIndices.get(HOURS+EXAM_LETTER)).getStringCellValue();
            exam = !examValue.isBlank() && examValue.equals(EXAM_LETTER);
        }
    }

    private void processTerm(Cell cell){
        String cellValue;
        if (cell.getCellType() == CellType.NUMERIC){
            cellValue = String.valueOf(cell.getNumericCellValue());
        } else {
            cellValue = cell.getStringCellValue();
        }

        if(cellValue.isEmpty()) {
            return;
        }
        this.semesterNumber = cellValue;
    }

    private void processTypeFaculty(Cell cell){
        String cellValue = cell.getStringCellValue();
        if(!cellValue.isEmpty()) {
            List<String> facultyTypeCycle = new ArrayList<>(List.of(cellValue.split("/")));
            this.cycle = facultyTypeCycle.get(2);
            this.type = facultyTypeCycle.get(1);
        }
    }

    private void processFieldOfStudy(Cell cell){
        if(cell.getStringCellValue().isEmpty()){
            return;
        }

        List<String> fieldAndSpecialisation = new ArrayList<>(List.of(cell.getStringCellValue().split("/")));
        fieldOfStudyName = fieldAndSpecialisation.get(0) + "_" + type;
        if(fieldAndSpecialisation.size() < 2){
            fieldAndSpecialisation.add("BRAK");
        }
        specialisationName = fieldAndSpecialisation.get(1).replace(" ", "");
    }

    private void processHeaderRow(Row headerRow, Row helper){
        String prefix = "";
        int teacherLecutureIterator = 0;
        int teacherIterator = 0;
        for(Cell cell: headerRow) {
            int cellIndex = cell.getColumnIndex();
            String cellValue;
            if (cell.getCellType() == CellType.NUMERIC) {
                cellValue = String.valueOf(cell.getNumericCellValue()).trim();
            } else {
                cellValue = cell.getStringCellValue().trim();
            }

            Cell helperCell = helper.getCell(cellIndex);
            String helperCellValue = "";
            if (helperCell.getCellType() == CellType.NUMERIC) {
                helperCellValue = String.valueOf(helperCell.getNumericCellValue()).trim();
            } else {
                helperCellValue = helperCell.getStringCellValue().trim();
            }

            if (cellValue.startsWith(NUM_HOURS) || (prefix.equals(HOURS) && cellValue.isEmpty())) {
                prefix = HOURS;
                cellValue = prefix + helperCellValue;
            } else if (cellValue.startsWith(NUM_GROUPS) || (prefix.equals(GROUPS) && cellValue.isEmpty())) {
                prefix = GROUPS;
                cellValue = prefix + helperCellValue;
            } else if (cellValue.endsWith(LECTURER) || (prefix.equals(LEC_TEACHER_PR) && cellValue.isEmpty())) {
                prefix = LEC_TEACHER_PR + teacherLecutureIterator++ + "_";
                cellValue = prefix + helperCellValue;
            } else if (cellValue.equals(TEACHER) || (prefix.equals(TEACHER_PR) && cellValue.isEmpty())) {
                prefix = TEACHER_PR + teacherIterator++ + "_";
                cellValue = prefix + helperCellValue;
            }
            columnIndices.put(cellValue, cellIndex);
        }
    }

    private <T> T assignIfNotNull(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }
}