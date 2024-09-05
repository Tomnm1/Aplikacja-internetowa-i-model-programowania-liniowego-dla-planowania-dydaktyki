package pl.poznan.put.planner_endpoints.JoinTables.Subjects_Groups;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Subject.Subject;

/**
 * Data model for Subjects_Groups join table
 */
@Entity
@Table(name="przedmiot_grupa")
@IdClass(Subjects_GroupsCompositeKey.class)
public class Subjects_Groups {
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false) // Bidirectional
    @JoinColumn(name = "grupa")
    public Group group;
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false) // Bidirectional
    @JoinColumn(name = "przedmiot")
    public Subject subject;
}
