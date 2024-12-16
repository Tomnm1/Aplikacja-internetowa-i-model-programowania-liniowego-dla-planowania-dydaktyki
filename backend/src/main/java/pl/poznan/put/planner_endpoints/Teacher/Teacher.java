package pl.poznan.put.planner_endpoints.Teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.jna.platform.unix.solaris.LibKstat;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectType_Teacher;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
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
    @Column(name = "second_name")
    public String secondName;
    @Column(name = "usos_id")
    public Integer usosId;
    @Column(name = "inner_id")
    public Integer innerId;
    @Column(name = "elogin_id")
    public String email;
    @Column(name = "is_admin")
    public Boolean isAdmin;
    @Column(name = "degree")
    @Convert(converter = DegreeConverter.class)
    public Degree degree;
    @Column(name = "preferences", columnDefinition = "json")
    @Type(JsonType.class)
    @Fetch(FetchMode.JOIN)
    public Map<String, String> preferences = new HashMap<>();
    @ManyToMany
    @JoinTable(
            name = "subject_type_teacher",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_type_id")
    )
    @JsonIgnore
    public List<SubjectType> subjectTypesList;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    public List<SubjectType_Teacher> subjectTypesTeacherList;

    public TeacherDTO convertToDTO(){
        TeacherDTO dto = new TeacherDTO();
        dto.id = this.id;
        dto.firstName = this.firstName;
        dto.secondName = this.secondName;
        dto.lastName = this.lastName;
        dto.email = this.email;
        dto.degree = this.degree;
        dto.isAdmin = this.isAdmin;
        dto.preferences = this.preferences;
        dto.subjectTypesList = this.subjectTypesTeacherList.stream().map(SubjectType_Teacher::getSubjetTyprId).collect(Collectors.toList());
        dto.usosId = this.usosId;
        dto.innerId = this.innerId;
        return dto;
    }
}
