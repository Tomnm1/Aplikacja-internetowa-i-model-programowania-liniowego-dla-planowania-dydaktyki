package pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

public interface ClassroomSubjectTypeRepository extends JpaRepository<ClassroomSubjectType, Integer> {
    List<ClassroomSubjectType> findBySubjectType(SubjectType subjectType);
}
