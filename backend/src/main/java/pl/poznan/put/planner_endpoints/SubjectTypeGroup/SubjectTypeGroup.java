package pl.poznan.put.planner_endpoints.SubjectTypeGroup;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

@Entity
@Table(name = "subject_types_groups")
public class SubjectTypeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @ManyToOne
    @JoinColumn(name = "subject_type_id")
    public SubjectType subjectType;
    @ManyToOne
    @JoinColumn(name = "group_id")
    public Group group;
}
