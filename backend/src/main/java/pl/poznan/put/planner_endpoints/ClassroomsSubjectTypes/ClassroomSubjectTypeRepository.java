package pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

public interface ClassroomSubjectTypeRepository extends JpaRepository<ClassroomSubjectType, Integer> {
    List<ClassroomSubjectType> findBySubjectType(SubjectType subjectType);

    @Query(value = "SELECT DISTINCT classroom_id FROM classrooms_subject_types", nativeQuery = true)
    List<Integer> getAllAssignedClassrooms();

    boolean existsByClassroomClassroomIDAndSubjectTypeSubjectTypeId(Integer classroom_classroomID, Integer subjectType_subjectTypeId);

    List<ClassroomSubjectType> findBySubjectType_SubjectTypeId(Integer subjectTypeSubjectTypeId);
}
