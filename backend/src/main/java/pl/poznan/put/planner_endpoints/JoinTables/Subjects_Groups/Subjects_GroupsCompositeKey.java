package pl.poznan.put.planner_endpoints.JoinTables.Subjects_Groups;

import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Subject.Subject;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for Subjects_Groups entity
 */
public class Subjects_GroupsCompositeKey implements Serializable {
    public Group group;
    public Subject subject;

    public Subjects_GroupsCompositeKey(){}

    public Subjects_GroupsCompositeKey(Group group, Subject subject){
        this.group = group;
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subjects_GroupsCompositeKey that = (Subjects_GroupsCompositeKey) o;
        return Objects.equals(group, that.group) && Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, subject);
    }
}
