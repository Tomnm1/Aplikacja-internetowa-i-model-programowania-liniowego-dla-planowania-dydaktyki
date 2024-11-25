package pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassroomSubjectTypeService {
    @Autowired
    private ClassroomSubjectTypeRepository classroomSubjectTypeRepository;

    public ClassroomSubjectType createClassroomSubjectType(ClassroomSubjectType classroomSubjectType){
        return classroomSubjectTypeRepository.save(classroomSubjectType);
    }
}
