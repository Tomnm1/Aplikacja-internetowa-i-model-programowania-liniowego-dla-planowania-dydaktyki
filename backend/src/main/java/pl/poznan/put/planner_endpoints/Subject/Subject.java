package pl.poznan.put.planner_endpoints.Subject;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Course.Course;

/**
 * Data model for subjects table
 */
@Entity
@Table(name="przedmioty")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "nazwa")
    public String name;
    @Column(name = "jezyk")
    public String language;
    @Column(name = "egzamin")
    public Boolean exam;
    @Column(name = "obieralny")
    public Boolean elective;
    @Column(name = "planowany")
    public Boolean planned;
    @JoinColumn(name = "kierunek")
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    public Course course;
}