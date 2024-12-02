package pl.poznan.put.data_import;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelToInnerIdTeacherService {
    private final int TEACHER_COLUMN = 0;
    private final int TEACHER_ID_COLUMN = 2;
    private List<String> result = new ArrayList<>();
    private final TeacherService teacherService;

    @Autowired
    ExcelToInnerIdTeacherService(
            TeacherService teacherService
    ){
        this.teacherService = teacherService;
    }

    public List<String> startSheetProcessing(Sheet sheet){
        for (int i = 0; i < sheet.getLastRowNum(); i++){
            Row row = sheet.getRow(i);
            if(row != null)
                processRow(row);
        }
        return this.result;
    }

    private void processRow(Row row){
        String teacherData = row.getCell(TEACHER_COLUMN).getStringCellValue().replace("_", " ");
        if (teacherData.isBlank() || teacherData.isEmpty()){
            return;
        }
        String[] teacherDataList = teacherData.replaceAll("  ", " ").split(" ");
        String lastName = teacherDataList[0].trim();
        String firstName = teacherDataList[1].trim();
        String secondName = (teacherDataList.length > 2 && teacherDataList[2] != null)
                ? teacherDataList[2].trim()
                : null;
        int innerId = (int) row.getCell(TEACHER_ID_COLUMN).getNumericCellValue();
        Teacher teacher = new Teacher();
        if(secondName != null){
            teacher = teacherService.findByFirstNameAndLastNameAndSecondName(firstName, secondName, lastName);
        } else {
            teacher = teacherService.findByFirstNameAndLastName(firstName, lastName);
        }

        if (teacher == null){
            this.result.add("Teacher " + firstName + " " + secondName + " " + lastName + " does not exist.");
        } else {
            teacher.innerId = innerId;
            teacherService.updateteacherByID(teacher.id, teacher);
        }
    }
}
