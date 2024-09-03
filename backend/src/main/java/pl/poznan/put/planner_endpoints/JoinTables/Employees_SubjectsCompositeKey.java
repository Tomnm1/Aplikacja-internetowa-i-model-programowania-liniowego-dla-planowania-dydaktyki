package pl.poznan.put.planner_endpoints.JoinTables;

import pl.poznan.put.planner_endpoints.Employee.Employee;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.io.Serializable;

/**
 * Composite key class for Employees_Subjects entity
 */
public class Employees_SubjectsCompositeKey implements Serializable {
    public SubjectType subjectType;
    public Employee employee;

    public Employees_SubjectsCompositeKey(){}

    public Employees_SubjectsCompositeKey(SubjectType subjectType, Employee employee){
        this.subjectType = subjectType;
        this.employee = employee;
    }

}
