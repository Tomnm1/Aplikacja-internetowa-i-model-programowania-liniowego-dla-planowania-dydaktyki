package pl.poznan.put.data_import;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.data_import.insert_to_db.*;
import pl.poznan.put.data_import.model.subjects.SubjectWithGroupData;
import pl.poznan.put.data_import.model.subjects.TeacherWithInnerId;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;
import pl.poznan.put.planner_endpoints.Subject.Language;
import pl.poznan.put.planner_endpoints.Subject.Subject;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static pl.poznan.put.constans.Constans.ExcelToDb.ColumnNames.*;
import static pl.poznan.put.constans.Constans.ExcelToDb.HeaderHelper.*;
import static pl.poznan.put.constans.Constans.ExcelToDb.HeaderHelper.Prefixes.*;
import static pl.poznan.put.constans.Constans.ExcelToDb.subjectTypeStudentQuantity.*;
import static pl.poznan.put.constans.Constans.HelperMethods.assignIfNotNull;

@Service
public class ExcelToDbService {
    private Sheet sheet;
    private Row row;
    private String columnName;

    private final Map<String, Integer> columnIndices = new HashMap<>();
    private final Map<String, Consumer<Cell>> columnActions = new LinkedHashMap<>();

    private final FieldOfStudyHandler fieldOfStudyHandler;
    private final SpecialisationHandler specialisationHandler;
    private final SemesterHandler semesterHandler;
    private final SubjectHandler subjectHandler;
    private final SubjectTypeHandler subjectTypeHandler;
    private final GroupsHandler groupsHandler;

    private String cycle = "";
    private String semesterNumber = "";
    private String type = ""; //stacjo - niestacjo
    private boolean exam;
    private Integer numStudents;
    private String subjectName = "";

    private FieldOfStudy fieldOfStudy;
    private String fieldOfStudyName;
    private Specialisation specialisation;
    private String specialisationName;
    private Semester semester;
    private Subject subject;
    private final List<SubjectType> subjectTypes = new ArrayList<>();
    private Map<String, Integer> groupsCounter;
    private final Map<String, List<SubjectWithGroupData>> groupTypesData = new LinkedHashMap<>();

    @Autowired
    public ExcelToDbService(
        FieldOfStudyHandler fieldOfStudyHandler,
        SpecialisationHandler specialisationHandler,
        SemesterHandler semesterHandler,
        SubjectHandler subjectHandler,
        SubjectTypeHandler subjectTypeHandler,
        GroupsHandler groupsHandler
    ){
        this.fieldOfStudyHandler = fieldOfStudyHandler;
        this.specialisationHandler = specialisationHandler;
        this.semesterHandler = semesterHandler;
        this.subjectHandler = subjectHandler;
        this.subjectTypeHandler = subjectTypeHandler;
        this.groupsHandler = groupsHandler;
    }
    public void startSheetProcessing(Sheet sheet){
        cycle = "";
        semesterNumber = "";
        type = "";
        exam = false;
        numStudents = MAX_LECTURE;
        subjectName = "";

        fieldOfStudy = null;
        fieldOfStudyName = "";
        specialisation = null;
        specialisationName = "";
        semester = null;
        subject = null;
        subjectTypes.clear();

        Row headerRow = sheet.getRow(2);
        Row headerRowHelper = sheet.getRow(3);
        processHeaderRow(headerRow, headerRowHelper);

        prepareColumnActions();

        processAllRows(sheet, 5);

    }

    private void insertData(){
        boolean groupsFlag = false;
        this.fieldOfStudy = assignIfNotNull(fieldOfStudyHandler.insertFieldOfStudy(fieldOfStudyName), this.fieldOfStudy);
        this.specialisation = assignIfNotNull(specialisationHandler.insertSpecialisation(specialisationName, cycle, fieldOfStudy), this.specialisation);
        Semester newSemester = semesterHandler.insertSemester(semesterNumber, specialisation);
        Semester tempSemester = this.semester;
        if ((this.semester != null
                && !Objects.equals(newSemester.specialisation.specialisationId, this.semester.specialisation.specialisationId))
                || (this.semester != null && Objects.equals(newSemester.specialisation.specialisationId, this.semester.specialisation.specialisationId)
                && !Objects.equals(newSemester.number, this.semester.number))){
            groupsFlag = true;
        }
        this.semester = newSemester;
//        this.semester = assignIfNotNull(semesterHandler.insertSemester(semesterNumber, specialisation), this.semester);
        this.subject = assignIfNotNull(subjectHandler.insertSubject(subjectName, exam, false, false, Language.polski, semester), this.subject);
        if(!subjectTypes.isEmpty())
            subjectTypeHandler.insertSubjectTypes(this.subjectTypes, this.subject);

        if(groupsFlag){
            List <SubjectWithGroupData> tempSubject = groupTypesData.get(subjectName);
            groupTypesData.remove(subjectName);
            this.groupsHandler.processAndInsertGroups(this.groupsCounter, tempSemester, this.groupTypesData);
            groupsCounter.clear();
            groupTypesData.clear();
            groupTypesData.put(subjectName, tempSubject);
        }
    }

    private void processAllRows(Sheet sheet, int startingRow){
        this.sheet = sheet;
        resetGroupsCounter();
        for (int i = startingRow; i < sheet.getLastRowNum(); i++){
            row = sheet.getRow(i);
            for (Map.Entry<String, Consumer<Cell>> entry : columnActions.entrySet()) {
                columnName = entry.getKey();
                Consumer<Cell> action = entry.getValue();
                int columnIndex = columnIndices.get(columnName);
                Cell cell = row.getCell(columnIndex);
                if(cell != null) {
                    action.accept(cell);
                }
            }
            insertData();
            subjectTypes.clear();
        }
    }

    private void prepareColumnActions(){
        columnActions.put(TYPE_FACULTY, this::processTypeFaculty);
        columnActions.put(FIELD_OF_STUDY, this::processFieldOfStudy);
        columnActions.put(TERM, this::processTerm);
        columnActions.put(SUBJECT, this::processSubject);
        columnActions.put(HOURS+ LECTURE_LETTER, this::processSubjectTypeLecture);
        columnActions.put(HOURS+EXERCISE_LETTER, this::processSubjectTypeExercise);
        columnActions.put(HOURS+LAB_LETTER, this::processSubjectTypeLab);
        columnActions.put(HOURS+PROJECT_LETTER, this::processSubjectTypeProject);
        columnActions.put(GROUPS+LECTURE_LETTER, this::processLectureGroups);
        columnActions.put(GROUPS+EXERCISE_DOT_LETTER, this::processExerciseGroups);
        columnActions.put(GROUPS+LAB_LETTER, this::processLabGroups);
        columnActions.put(GROUPS+PROJECT_LETTER, this::processProjectGroups);
    }

    private void processSubject(Cell cell){
        String cellValue = cell.getStringCellValue();
        if(!cellValue.isEmpty()) {
            subjectName = cellValue;
            String examValue = row.getCell(columnIndices.get(HOURS+EXAM_LETTER)).getStringCellValue();
            numStudents = (int) row.getCell(columnIndices.get(HOURS)).getNumericCellValue();
            exam = !examValue.isBlank() && examValue.equals(EXAM_LETTER);
        }
    }

    private void processSubjectTypeLecture(Cell cell){
        int cellValue = (int) cell.getNumericCellValue();
        Cell groupCell = this.row.getCell(columnIndices.get(GROUPS + LECTURE_LETTER));
        int numGroups = 0;
        if (groupCell != null && groupCell.getCellType() == CellType.NUMERIC) {
            numGroups = (int) groupCell.getNumericCellValue();
        }

        if(cellValue != 0) {
            createSubjectType(ClassTypeOwn.wykład, cellValue, MAX_LECTURE, numGroups);
        }
    }

    private void processSubjectTypeExercise(Cell cell){
        int cellValue = (int) cell.getNumericCellValue();
        Cell groupCell = this.row.getCell(columnIndices.get(GROUPS + EXERCISE_DOT_LETTER));
        int numGroups = 0;
        if (groupCell != null && groupCell.getCellType() == CellType.NUMERIC) {
            numGroups = (int) groupCell.getNumericCellValue();
        }
        if(cellValue != 0) {
            createSubjectType(ClassTypeOwn.ćwiczenia, cellValue, MAX_EXERCISE, numGroups);
        }
    }

    private void processSubjectTypeLab(Cell cell){
        int cellValue = (int) cell.getNumericCellValue();
        Cell groupCell = this.row.getCell(columnIndices.get(GROUPS + LAB_LETTER));
        int numGroups = 0;
        if (groupCell != null && groupCell.getCellType() == CellType.NUMERIC) {
            numGroups = (int) groupCell.getNumericCellValue();
        }
        if(cellValue != 0) {
            createSubjectType(ClassTypeOwn.laboratoria, cellValue, MAX_LABORATORY, numGroups);
        }
    }

    private void processSubjectTypeProject(Cell cell){
        int cellValue = (int) cell.getNumericCellValue();
        Cell groupCell = this.row.getCell(columnIndices.get(GROUPS + PROJECT_LETTER));
        int numGroups = 0;
        if (groupCell != null && groupCell.getCellType() == CellType.NUMERIC) {
            numGroups = (int) groupCell.getNumericCellValue();
        }
        if(cellValue != 0) {
            createSubjectType(ClassTypeOwn.projekt, cellValue, MAX_PROJECT, numGroups);
        }
    }

    private void createSubjectType(ClassTypeOwn type, int cellValue, int maxNumberOfStudents, int numGroups){
        SubjectType subjectType = new SubjectType();
        subjectType.type = type;
        subjectType.numOfHours = cellValue;
        subjectType.maxStudentsPerGroup = maxNumberOfStudents;
        subjectType.groupsList = new ArrayList<>();
        subjectType.teachersList = new ArrayList<>();
        if (type == ClassTypeOwn.wykład){
            subjectType.maxStudentsPerGroup = numStudents;
        }else{
            subjectType.maxStudentsPerGroup = numStudents / numGroups;
        }
        subjectTypes.add(subjectType);

        List<TeacherWithInnerId> assignedTeachers = new ArrayList<>();
        if(type == ClassTypeOwn.wykład){
            assignedTeachers = assignTeachersForLecture();
        } else {
            assignedTeachers = assignTeachersForOtherClasses(subjectType, numGroups);
        }

        SubjectWithGroupData subjectWithGroupData = new SubjectWithGroupData(subjectType, type, numGroups, assignedTeachers);
        this.groupTypesData.computeIfAbsent(this.subjectName, k -> new ArrayList<>())
                .add(subjectWithGroupData);
    }

    private List<TeacherWithInnerId> assignTeachersForLecture(){
        List<TeacherWithInnerId> assignedTeachers = new ArrayList<>();
        List<Integer> filteredColumnIndices = columnIndices.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(LEC_TEACHER_PR))
                .map(Map.Entry::getValue)
                .toList();

        for(Integer columnIndex: filteredColumnIndices){
            String teacherInnerId = getCellValue(columnIndex);

            String teacherHours = getCellValue(columnIndex + 1);

            if (!teacherInnerId.isEmpty() && !teacherHours.isEmpty()) {
                assignedTeachers.add(new TeacherWithInnerId(teacherInnerId, Integer.parseInt(teacherHours)));
            }
        }
        if(assignedTeachers.isEmpty()){
            assignedTeachers.add(new TeacherWithInnerId("123456789", 123456789)); // dummy
        }
        return assignedTeachers;
    }

    private List<TeacherWithInnerId> assignTeachersForOtherClasses(SubjectType subjectType, int numGroups){
        List<TeacherWithInnerId> assignedTeachers = new ArrayList<>();
        List<Integer> filteredColumnIndices = columnIndices.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(TEACHER_PR))
                .map(Map.Entry::getValue)
                .toList();

        for(Integer columnIndex: filteredColumnIndices){
            String teacherInnerId = getCellValue(columnIndex);

            String teacherHours = getCellValue(columnIndex + 1);

            if (!teacherInnerId.isEmpty() && !teacherHours.isEmpty()) {
                assignedTeachers.add(new TeacherWithInnerId(teacherInnerId, Integer.parseInt(teacherHours)));
            }
        }
        if(assignedTeachers.isEmpty()){
            assignedTeachers.add(new TeacherWithInnerId("123456789", 123456789)); // dummy
            return assignedTeachers;
        } else {
            int missingHours = 0;
            try {
                missingHours = Integer.parseInt(getCellValue(columnIndices.get(SUMMARY)));
            } catch(java.lang.NumberFormatException ignored){}

            Integer totalNumHours = subjectType.numOfHours * numGroups - missingHours;
            List<TeacherWithInnerId> newAssignedTeachers = findMatchingTeachers(assignedTeachers, totalNumHours);
            if (newAssignedTeachers.isEmpty()){
                int fixedHours = subjectType.numOfHours * numGroups / assignedTeachers.size();
                for (TeacherWithInnerId teacherWithInnerId: assignedTeachers){
                    teacherWithInnerId.setNumHours(fixedHours);
                }
                return assignedTeachers;
            }
            else if (missingHours != 0){
                int toAdd = missingHours / subjectType.numOfHours;
                for(int i = 0; i < toAdd; i++){
                    TeacherWithInnerId teacher = newAssignedTeachers.get(0);
                    teacher.setNumHours(teacher.getNumHours() + subjectType.numOfHours);
                }
            }
            return newAssignedTeachers;
        }
    }

    private List<TeacherWithInnerId> findMatchingTeachers(List<TeacherWithInnerId> teachers, Integer totalHours) {
        List<TeacherWithInnerId> numericTeachers = teachers.stream()
                .map(t -> new TeacherWithInnerId(t.getInnerId(), t.getNumHours()))
                .collect(Collectors.toList());

        List<TeacherWithInnerId> result = new ArrayList<>();
        findCombination(numericTeachers, totalHours, new ArrayList<>(), result);
        return result;
    }

    private void findCombination(List<TeacherWithInnerId> teachers, int target, List<TeacherWithInnerId> current, List<TeacherWithInnerId> result) {
        int sum = current.stream().mapToInt(TeacherWithInnerId::getNumHours).sum();

        if (sum == target) {
            result.addAll(current);
            return;
        }

        if (sum > target) {
            return;
        }

        for (int i = 0; i < teachers.size(); i++) {
            List<TeacherWithInnerId> remaining = new ArrayList<>(teachers.subList(i + 1, teachers.size()));
            List<TeacherWithInnerId> newCurrent = new ArrayList<>(current);
            newCurrent.add(teachers.get(i));
            findCombination(remaining, target, newCurrent, result);
            if (!result.isEmpty()) return;
        }
    }

    private String getCellValue(int index){
        Cell cell = this.row.getCell(index);
        String result = "";
        if (cell != null) {
            result = switch (cell.getCellType()) {
                case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
                case STRING -> cell.getStringCellValue();
                default -> "";
            };
        }
        return result;
    }

    private void processLectureGroups(Cell cell){
        int cellValue = (int) cell.getNumericCellValue();
        if(cellValue != 0) {
            setGroups(LECTURE_LETTER, cellValue);
        }
    }

    private void processExerciseGroups(Cell cell){
        int cellValue = (int) cell.getNumericCellValue();
        if(cellValue != 0) {
            setGroups(EXERCISE_LETTER, cellValue);
        }
    }

    private void processLabGroups(Cell cell){
        int cellValue = (int) cell.getNumericCellValue();
        if(cellValue != 0) {
            setGroups(LAB_LETTER, cellValue);
        }
    }

    private void processProjectGroups(Cell cell){
        int cellValue = (int) cell.getNumericCellValue();
        if(cellValue != 0) {
            setGroups(PROJECT_LETTER, cellValue);
        }
    }

    private void setGroups(String key, int val){
        groupsCounter.merge(key, val, Math::max);
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

    private void resetGroupsCounter(){
        groupsCounter = new HashMap<>();
        groupsCounter.put(LECTURE_LETTER, 0);
        groupsCounter.put(EXERCISE_LETTER, 0);
        groupsCounter.put(LAB_LETTER, 0);
        groupsCounter.put(PROJECT_LETTER, 0);
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
}