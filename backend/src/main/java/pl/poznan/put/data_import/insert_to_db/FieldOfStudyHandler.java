package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudyService;

import java.util.Objects;

import static pl.poznan.put.constans.Constans.FieldsOfStudyTypes.FULL_TIME;
import static pl.poznan.put.constans.Constans.FieldsOfStudyTypes.PART_TIME;

@Component
public class FieldOfStudyHandler {
    private final FieldOfStudyService fieldOfStudyService;

    @Autowired
    public FieldOfStudyHandler(
        FieldOfStudyService fieldOfStudyService
    ){
        this.fieldOfStudyService = fieldOfStudyService;
    }

    public FieldOfStudy insertFieldOfStudy(String name, String typ){
        FieldOfStudy fieldOfStudy = new FieldOfStudy();
        fieldOfStudy.name = name;
        if(Objects.equals(typ, "N"))
            fieldOfStudy.typ = PART_TIME;
        else if (Objects.equals(typ, "S"))
            fieldOfStudy.typ = FULL_TIME;
        return fieldOfStudyService.createFieldOfStudyIfNotExists(fieldOfStudy);
    }
}
