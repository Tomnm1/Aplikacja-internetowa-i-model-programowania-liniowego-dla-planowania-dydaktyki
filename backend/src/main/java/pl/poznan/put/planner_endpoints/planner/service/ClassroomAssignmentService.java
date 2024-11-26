package pl.poznan.put.planner_endpoints.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Classroom.Classroom;
import pl.poznan.put.planner_endpoints.Classroom.ClassroomService;
import pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes.ClassroomSubjectType;
import pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes.ClassroomSubjectTypeService;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectTypeService;

import java.util.List;

@Service
public class ClassroomAssignmentService {
    private ClassroomSubjectTypeService classroomSubjectTypeService;
    private SubjectTypeService subjectTypeService;
    private ClassroomService classroomService;

    @Autowired
    ClassroomAssignmentService(
            ClassroomSubjectTypeService classroomSubjectTypeService,
            SubjectTypeService subjectTypeService,
            ClassroomService classroomService
    ){
        this.classroomSubjectTypeService = classroomSubjectTypeService;
        this.subjectTypeService = subjectTypeService;
        this.classroomService = classroomService;
    }
    public void assignClassroomsBasedOnCapacity(){
        List<SubjectType> subjectTypeList = subjectTypeService.getAllsubjectType();
        for(SubjectType subjectType: subjectTypeList){
            List<Classroom> classrooms = classroomService.findRandomClassroomWithCapacity(subjectType.maxStudentsPerGroup);
            for(Classroom classroom: classrooms)
                createClassroomSubjectType(classroom, subjectType);
        }
    }

    private void createClassroomSubjectType(Classroom classroom, SubjectType subjectType){
        ClassroomSubjectType classroomSubjectType = new ClassroomSubjectType();
        classroomSubjectType.classroom = classroom;
        classroomSubjectType.subjectType = subjectType;
        classroomSubjectTypeService.createClassroomSubjectType(classroomSubjectType);
    }
}
