package pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

@Service
public class ClassroomSubjectTypeService {
    @Autowired
    private ClassroomSubjectTypeRepository classroomSubjectTypeRepository;

    public ClassroomSubjectType createClassroomSubjectType(ClassroomSubjectType classroomSubjectType){
        return classroomSubjectTypeRepository.save(classroomSubjectType);
    }

    public List<ClassroomSubjectType> findBySubjectType(SubjectType subjectType){
        return classroomSubjectTypeRepository.findBySubjectType(subjectType);
    }
}
