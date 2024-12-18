package pl.poznan.put.planner_endpoints.SubjectTypeTeacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;

import java.util.List;

public interface SubjectTypeTeacherRepository extends JpaRepository<SubjectTypeTeacher, Integer> {
    List<SubjectTypeTeacher> findBySubjectType(SubjectType subjectType);

    List<SubjectTypeTeacher> findByTeacher(Teacher teacher);

    @Query(value = "SELECT * FROM subject_type_teacher stt WHERE stt.teacher_id IN (SELECT DISTINCT h.teacher_id FROM subject_type_teacher h)", nativeQuery = true)
    List<SubjectTypeTeacher> findAllAssignedTeachers();

    @Query(value = "SELECT DISTINCT teacher_id FROM subject_type_teacher", nativeQuery = true)
    List<Integer> findAllAssignedTeachersIds();

    boolean existsByTeacherIdAndSubjectTypeSubjectTypeId(Integer teacher_id, Integer subjectType_subjectTypeId);
}
