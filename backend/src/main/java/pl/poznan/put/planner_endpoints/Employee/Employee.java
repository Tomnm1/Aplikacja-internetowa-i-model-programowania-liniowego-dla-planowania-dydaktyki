package pl.poznan.put.planner_endpoints.Employee;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import pl.poznan.put.planner_endpoints.JoinTables.Employees_Subjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data model for Employees table
 */
@Entity
@Table(name="pracownicy")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "imie")
    public String name;
    @Column(name = "nazwisko")
    public String surname;
    @Column(name = "stopien")
    public String degree;
    @Column(name = "preferencje", columnDefinition = "json")
    @Type(JsonType.class)
    public Map<String, String> preferences = new HashMap<>();
    // jak będzie uspecifikowane dokładnie: @Convert(converter = prefConverter.class)
    @OneToMany(mappedBy = "employee") // Bidirectional
    List<Employees_Subjects> employees_subjectsList;
}