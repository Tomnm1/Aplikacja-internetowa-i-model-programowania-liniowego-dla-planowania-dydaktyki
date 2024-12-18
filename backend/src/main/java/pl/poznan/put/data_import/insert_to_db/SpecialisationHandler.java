package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.constans.Constants;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.Specialisation.Cycle;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;
import pl.poznan.put.planner_endpoints.Specialisation.SpecialisationService;

@Component
public class SpecialisationHandler {
    private final SpecialisationService specialisationService;

    @Autowired
    public SpecialisationHandler(
            SpecialisationService specialisationService
    ){
        this.specialisationService = specialisationService;
    }

    public Specialisation insertSpecialisation(String name, String cycle, FieldOfStudy fieldOfStudy){
        Specialisation specialisation = new Specialisation();
        specialisation.name = name;
        specialisation.cycle = Constants.EnumUtils.fromString(Cycle.class, cycle);
        specialisation.fieldOfStudy = fieldOfStudy;
        return specialisationService.createSpecialisationIfNotExists(specialisation);
    }
}
