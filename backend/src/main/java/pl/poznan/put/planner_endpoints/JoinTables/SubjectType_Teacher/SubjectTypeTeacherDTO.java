package pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;

public class SubjectTypeTeacherDTO {

    public Integer id;

    public Integer teacherId;

    public String teacherFirstName;

    public String teacherSecondName;

    public String teacherLastName;

    public Integer subjectTypeId;

    public Integer numHours;

}
