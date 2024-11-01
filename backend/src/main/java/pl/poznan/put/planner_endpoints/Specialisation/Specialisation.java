package pl.poznan.put.planner_endpoints.Specialisation;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.Subject.Language;

@Entity
@Table(name="specialisations")
public class Specialisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialisation_id")
    public Integer SpecialisationId;
    @Column(name = "name")
    public String name;
    @Column(name = "cycle")
    public Cycle cycle;
    @JoinColumn(name = "field_of_study_id")
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    public FieldOfStudy fieldOfStudy;
}
