package pl.poznan.put.planner_endpoints.JoinTables.Employees_Subjects;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Teacher.Employee;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

/**
 * Data model for Employees_sybjects join table
 */
@Entity
@Table(name="prowadzacy_przedmioty")
@IdClass(Employees_SubjectsCompositeKey.class)
public class Employees_Subjects {
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false) // Bidirectional
    @JoinColumn(name = "przedmiot")
    public SubjectType subjectType;
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false) // Bidirectional
    @JoinColumn(name = "prowadzacy")
    public Employee employee;
    @Column(name = "liczba_grup")
    public Integer numOfGroups;
}
