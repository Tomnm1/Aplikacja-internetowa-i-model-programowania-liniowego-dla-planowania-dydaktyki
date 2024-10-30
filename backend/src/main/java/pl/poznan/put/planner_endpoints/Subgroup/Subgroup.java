package pl.poznan.put.planner_endpoints.Subgroup;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Group.Group;

/**
 * Data model for subgroups table
 */
@Entity
@Table(name = "subgroups")
public class Subgroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "group_id")
    public Group group;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "subgroup_id")
    public Group subgroup;
}
