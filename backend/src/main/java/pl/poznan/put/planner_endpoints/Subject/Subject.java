package pl.poznan.put.planner_endpoints.Subject;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Semester.Semester;

/**
 * Data model for subjects table
 */
@Entity
@Table(name="subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    public Integer SubjectId;
    @Column(name = "name")
    public String name;
    @Column(name = "language")
    public Language language;
    @Column(name = "exam")
    public Boolean exam;
    @Column(name = "mandatory")
    public Boolean mandatory;
    @Column(name = "planned")
    public Boolean planned;
    @JoinColumn(name = "semester_id")
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    public Semester semester;
}