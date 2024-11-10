package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Semester.SemesterService;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;

@Component
public class SemesterHandler {
    private final SemesterService semesterService;

    @Autowired
    public SemesterHandler(
        SemesterService semesterService
    ){
        this.semesterService = semesterService;
    }

    public Semester insertSemester(String number, Specialisation specialisation){
        Semester semester = new Semester();
        semester.number = number;
        semester.specialisation = specialisation;
        return semesterService.createSemesterIfNotExists(semester);
    }
}
