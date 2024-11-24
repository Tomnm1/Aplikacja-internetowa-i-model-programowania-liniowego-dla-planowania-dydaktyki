package pl.poznan.put.data_import.model.subjects;

import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

public record SubjectWithGroupData(
        SubjectType subjectType,
        ClassTypeOwn type,
        int numGroups,
        List<TeacherWithInnerId> assignedTeachers) {
}
