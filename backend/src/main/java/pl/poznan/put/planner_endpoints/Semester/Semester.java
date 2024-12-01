package pl.poznan.put.planner_endpoints.Semester;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;

import java.util.List;

@Entity
@Table(name="semesters")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    public Integer semesterId;
    @Column(name = "number")
    public String number;
    @JoinColumn(name = "specialisation_id")
    @ManyToOne
    public Specialisation specialisation;
    @Column(name = "typ")
    public String typ;
}
