package pl.poznan.put.planner_endpoints.SubjectType;

import pl.poznan.put.planner_endpoints.Classroom.Classroom;
import pl.poznan.put.planner_endpoints.Group.GroupDTO;
import pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher.SubjectTypeTeacherDTO;
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
    public List<GroupDTO> groupsList;
    public List<Classroom> classroomList;

}
