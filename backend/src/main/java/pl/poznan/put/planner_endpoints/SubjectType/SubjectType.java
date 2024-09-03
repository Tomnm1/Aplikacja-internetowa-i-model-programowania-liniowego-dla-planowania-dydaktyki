package pl.poznan.put.planner_endpoints.SubjectType;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Subject.Subject;

@Entity
@Table(name = "formy przedmiotow")
public class SubjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "id_przedmiotu")
    public Subject subject;
    @Column(name = "liczba_godzin")
    public Integer numOfHours;
    @Column(name = "typ")
    public String type;
    @Column(name = "max_osob_w_grupie")
    public Integer maxStudentsPerGroup;
}