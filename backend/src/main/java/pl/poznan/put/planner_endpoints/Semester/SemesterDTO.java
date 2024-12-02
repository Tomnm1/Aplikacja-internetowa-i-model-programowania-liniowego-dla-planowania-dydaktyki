package pl.poznan.put.planner_endpoints.Semester;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;

public class SemesterDTO {
    public Integer semesterId;
    public String number;
    public String typ;
    public Specialisation specialisation;
    public Long groupCount;

    public SemesterDTO(Integer semesterId,String number,String typ, Specialisation specialisation,Long gc){
        this.semesterId = semesterId;
        this.number = number;
        this.typ = typ;
        this.specialisation = specialisation;
        this.groupCount = gc;
    }

    public Semester toSemester(){
        Semester sem = new Semester();
        sem.semesterId = this.semesterId;
        sem.number = this.number;
        sem.typ = this.typ;
        sem.specialisation = this.specialisation;
        return sem;
    }
}
