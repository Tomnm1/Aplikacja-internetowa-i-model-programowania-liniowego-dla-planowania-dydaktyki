package pl.poznan.put.planner_endpoints.Teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data model for teachers table
 */
@Entity
@Table(name="teachers")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    public Integer id;
    @Column(name = "first_name")
    public String firstName;
    @Column(name = "last_name")
    public String lastName;
    @Column(name = "second_name")
    public String secondName;
    @Column(name = "usos_id")
    public Integer usosId;
    @Column(name = "inner_id")
    public Integer innerId;

    @Column(name = "degree")
    @Convert(converter = DegreeConverter.class)
    public Degree degree;
    @Column(name = "preferences", columnDefinition = "json")
    @Type(JsonType.class)
    public Map<String, String> preferences = new HashMap<>();
    @ManyToMany
    @JoinTable(
            name = "subject_type_teacher",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_type_id")
    )
    @JsonIgnore
    public List<SubjectType> subjectTypesList;
}
