package pl.poznan.put.planner_endpoints.SubjectType;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Subject.Subject;

@Entity
@Table(name = "subject_types")
public class SubjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_types_id")
    public Integer subjectTypeId;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "subject_id")
    public Subject subject;
    @Column(name = "number_of_hours")
    public Integer numOfHours;
    @Column(name = "type")
    public ClassTypeOwn type;
    @Column(name = "max_students")
    public Integer maxStudentsPerGroup;
}