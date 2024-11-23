package pl.poznan.put.planner_endpoints.SubjectType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectTypeTeacherDTO;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectType_Teacher;
import pl.poznan.put.planner_endpoints.Subject.Subject;

import java.util.List;
import java.util.stream.Collectors;

public class SubjectTypeDTO {
    public Integer subjectTypeId;

    public Subject subject;

    public Integer numOfHours;

    public ClassTypeOwn type;

    public Integer maxStudentsPerGroup;

    public List<SubjectTypeTeacherDTO> teachersList;

}
