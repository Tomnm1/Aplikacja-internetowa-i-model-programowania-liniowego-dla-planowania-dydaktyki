package pl.poznan.put.planner_endpoints.JoinTables.Employees_Subjects;

import pl.poznan.put.planner_endpoints.Teacher.Employee;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for Subjects_Groups entity
 */
public class Employees_SubjectsCompositeKey implements Serializable {
    public SubjectType subjectType;
    public Employee employee;

    public Employees_SubjectsCompositeKey(){}

    public Employees_SubjectsCompositeKey(SubjectType subjectType, Employee employee){
        this.subjectType = subjectType;
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employees_SubjectsCompositeKey that = (Employees_SubjectsCompositeKey) o;
        return Objects.equals(subjectType, that.subjectType) && Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectType, employee);
    }
}
