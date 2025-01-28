package pl.poznan.put.planner_endpoints.SubjectTypeTeacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;

import java.util.List;

@Service
public class SubjectTypeTeacherService {
    @Autowired
    private SubjectTypeTeacherRepository subjectTypeTeacherRepository;

    public SubjectTypeTeacher createSubjectTypeTeacher(SubjectTypeTeacher subjectTypeTeacher){
        return subjectTypeTeacherRepository.save(subjectTypeTeacher);
    }

    public List<SubjectTypeTeacher> findBySubjectType(SubjectType subjectType){
        return subjectTypeTeacherRepository.findBySubjectType(subjectType);
    }

    public List<SubjectTypeTeacher> findByTeacher(Teacher teacher){
        return subjectTypeTeacherRepository.findByTeacher(teacher);
    }

    public List<Integer> findAllAssignedTeachersIds(){
        return subjectTypeTeacherRepository.findAllAssignedTeachersIds();
    }

    public boolean teacherCanTeach(int teacherId, int subjectTypeId){
        return subjectTypeTeacherRepository.existsByTeacherIdAndSubjectTypeSubjectTypeId(teacherId, subjectTypeId);
    }

    public List<SubjectTypeTeacher> getAllSubjectTypeTeacher(){
        return subjectTypeTeacherRepository.findAll();
    }
}
