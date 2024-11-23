package pl.poznan.put.planner_endpoints.SubjectType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectType_Teacher;
import pl.poznan.put.planner_endpoints.Subject.Subject;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "subject_types")
public class SubjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_types_id")
    public Integer subjectTypeId;
    @ManyToOne // Unidirectional
    @JoinColumn(name = "subject_id")
    public Subject subject;
    @Column(name = "number_of_hours")
    public Integer numOfHours;
    @Column(name = "type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    public ClassTypeOwn type;
    @Column(name = "max_students")
    public Integer maxStudentsPerGroup;
    @ManyToMany(mappedBy = "subjectTypesList")
    @JsonIgnore
    public List<Group> groupsList;

    @OneToMany(mappedBy = "subjectType")
    @JsonIgnore
    public List<SubjectType_Teacher> teachersList;

    public SubjectTypeDTO convertToDTO(){
        SubjectTypeDTO dto = new SubjectTypeDTO();
        dto.subjectTypeId = this.subjectTypeId;
        dto.subject = this.subject;
        dto.type = this.type;
        dto.maxStudentsPerGroup = this.maxStudentsPerGroup;
        dto.numOfHours = this.numOfHours;
        dto.teachersList = this.teachersList.stream().map(SubjectType_Teacher::toSubjectTypeTeacherDTO).collect(Collectors.toList());
        return dto;
    }
}