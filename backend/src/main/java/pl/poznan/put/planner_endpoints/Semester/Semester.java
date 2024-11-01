package pl.poznan.put.planner_endpoints.Semester;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;

@Entity
@Table(name="semesters")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    public Integer semesterId;
    @Column(name = "number")
    public Short number;
    @JoinColumn(name = "specialisation_id")
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    public Specialisation specialisation;
}
