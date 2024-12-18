package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.constans.Constants;
import pl.poznan.put.planner_endpoints.Teacher.Degree;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;
import pl.poznan.put.data_import.model.worker.Worker;

import java.util.List;

@Component
public class TeacherHandler {
    private final TeacherService teacherService;

    @Autowired
    public TeacherHandler(
        TeacherService teacherService
    ){
        this.teacherService = teacherService;
    }

    public void insertTeachers(List<Worker> workers){
        for(Worker worker: workers){
            Teacher teacher = new Teacher();
            teacher.firstName = worker.getName();
            teacher.lastName = worker.getSurname();
            teacher.usosId = worker.getWorkerId();
            try {
                teacher.degree = Constants.EnumUtils.fromString(Degree.class, worker.getTitleDegree());
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown degree: " + worker.getTitleDegree());
                teacher.degree = Degree.BRAK;
            }
            Teacher teacher1 = teacherService.findByUsosId(teacher.usosId);
            if (teacher1 == null) {
                teacherService.createteacher(teacher);
            } else {
                System.out.println("Teacher with usosId: " + teacher.usosId + " already exists.");
            }
        }
    }
}
