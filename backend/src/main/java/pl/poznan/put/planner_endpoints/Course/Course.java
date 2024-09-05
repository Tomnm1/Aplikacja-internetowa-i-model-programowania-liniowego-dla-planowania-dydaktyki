package pl.poznan.put.planner_endpoints.Course;

import jakarta.persistence.*;

/**
 * Data model fot courses table
 */
@Entity
@Table(name = "kierunki")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "kierunek")
    public String course;
    @Column(name = "specjalizacja")
    public String specialization;
    @Column(name = "semestr")
    public Integer semester;
    @Column(name = "liczba_studentow")
    public Integer numOfStudents;
}
