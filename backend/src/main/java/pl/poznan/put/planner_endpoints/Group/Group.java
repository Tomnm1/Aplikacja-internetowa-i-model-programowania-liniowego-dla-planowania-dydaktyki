package pl.poznan.put.planner_endpoints.Group;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Course.Course;

/**
 * Data model fot groups table
 */
@Entity
@Table(name = "grupy")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "numer")
    public Integer number;
    @Column(name = "liczba_studentow")
    public Integer numOfStudents;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "kierunek")
    public Course course;
}
