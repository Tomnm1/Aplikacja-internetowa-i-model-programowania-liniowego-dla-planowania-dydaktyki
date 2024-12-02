package pl.poznan.put.planner_endpoints.Group;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectType_Teacher;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

/**
 * Data model for groups table
 */
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    public Integer id;
    @Column(name = "code")
    public String code;
    @Column(name = "group_type")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    public ClassTypeOwn group_type;
    @JoinColumn(name = "semester_id")
    @ManyToOne
    public Semester semester;

    @ManyToMany(mappedBy = "groupsList")
    @JsonIgnore
    public List<SubjectType> subjectTypesList;

    public GroupDTO toDTO(){
        GroupDTO dto = new GroupDTO();
        dto.id = id;
        dto.code = code;
        dto.group_type = group_type;
        return dto;
    }

    @Override
    public boolean equals(Object obj) {
        Group g = (Group) obj;
        return (g.id == this.id);
    }

}
