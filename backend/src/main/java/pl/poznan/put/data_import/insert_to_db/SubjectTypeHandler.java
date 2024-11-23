package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.Subject.Subject;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectTypeService;

import java.util.List;

@Component
public class SubjectTypeHandler {
    private final SubjectTypeService subjectTypeService;

    @Autowired
    public SubjectTypeHandler(
            SubjectTypeService subjectTypeService
    ){
        this.subjectTypeService = subjectTypeService;
    }

    public void insertSubjectTypes(List<SubjectType> subjectTypeList, Subject subject){
        for (SubjectType subjectType: subjectTypeList){
            subjectType.subject = subject;
            subjectTypeService.createsubjectType(subjectType.convertToDTO());
        }
    }
}
