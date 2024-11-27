package pl.poznan.put.planner_endpoints.Semester;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;

public class SemesterDTO {
    public Integer semesterId;
    public String number;
    public Specialisation specialisation;
    public Long groupCount;

    public SemesterDTO(Integer semesterId,String number,Specialisation specialisation,Long gc){
        this.semesterId = semesterId;
        this.number = number;
        this.specialisation = specialisation;
        this.groupCount = gc;
    }
}
