package pl.poznan.put.planner_endpoints.Specialisation;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;
import pl.poznan.put.planner_endpoints.Subject.Language;

@Entity
@Table(name="specialisations")
public class Specialisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialisation_id")
    public Integer specialisationId;
    @Column(name = "name")
    public String name;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "cycle")
    public Cycle cycle;
    @JoinColumn(name = "field_of_study_id")
    @ManyToOne
    public FieldOfStudy fieldOfStudy;
}
