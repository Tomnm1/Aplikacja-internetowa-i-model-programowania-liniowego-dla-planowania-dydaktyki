package pl.poznan.put.planner_endpoints.SubjectTypeTeacher;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;

import java.util.List;

public interface SubjectTypeTeacherRepository extends JpaRepository<SubjectTypeTeacher, Integer> {
    List<SubjectTypeTeacher> findBySubjectType(SubjectType subjectType);

    List<SubjectTypeTeacher> findByTeacher(Teacher teacher);
}
