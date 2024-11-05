package pl.poznan.put.planner_endpoints.Group;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;
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
    @JoinColumn(name = "semester_id")
    @ManyToOne
    public Semester semester;
    @ManyToMany
    @JoinTable(
            name = "subject_types_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_type_id")
    )
    public List<SubjectType> subjectTypesList;

}
