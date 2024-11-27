package pl.poznan.put.planner_endpoints.Teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.jna.platform.unix.solaris.LibKstat;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectType_Teacher;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(name = "degree")
    @Convert(converter = DegreeConverter.class)
    public Degree degree;
    @Column(name = "preferences", columnDefinition = "json")
    @Type(JsonType.class)
    public Map<String, String> preferences = new HashMap<>();

    @OneToMany(mappedBy = "teacher")
    public List<SubjectType_Teacher> subjectTypesList;

    public TeacherDTO convertToDTO(){
        TeacherDTO dto = new TeacherDTO();
        dto.id = this.id;
        dto.firstName = this.firstName;
        dto.lastName = this.lastName;
        dto.degree = this.degree;
        dto.subjectTypesList = this.subjectTypesList.stream().map(SubjectType_Teacher::getSubjetTyprId).collect(Collectors.toList());
        return dto;
    }
}
