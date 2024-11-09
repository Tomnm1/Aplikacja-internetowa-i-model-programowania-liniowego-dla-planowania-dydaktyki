package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudyService;

@Component
public class FieldOfStudyHandler {
    private final FieldOfStudyService fieldOfStudyService;

    @Autowired
    public FieldOfStudyHandler(
        FieldOfStudyService fieldOfStudyService
    ){
        this.fieldOfStudyService = fieldOfStudyService;
    }

    public FieldOfStudy insertFieldOfStudy(String name){
        FieldOfStudy fieldOfStudy = new FieldOfStudy();
        fieldOfStudy.name = name;
        return fieldOfStudyService.createFieldOfStudyIfNotExists(fieldOfStudy);
    }
}
