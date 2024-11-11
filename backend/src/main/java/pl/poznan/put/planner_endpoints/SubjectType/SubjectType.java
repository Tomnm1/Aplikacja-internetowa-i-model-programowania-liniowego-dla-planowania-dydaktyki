package pl.poznan.put.planner_endpoints.SubjectType;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Subject.Subject;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;

import java.util.List;

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
    public List<Group> groupsList;
    @ManyToMany(mappedBy = "subjectTypesList")
    public List<Teacher> teachersList;
}