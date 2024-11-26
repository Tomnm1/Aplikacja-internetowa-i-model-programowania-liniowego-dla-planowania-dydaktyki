package pl.poznan.put.planner_endpoints.SubjectTypeGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

public interface SubjectTypeGroupRepository extends JpaRepository<SubjectTypeGroup, Integer> {
    List<SubjectTypeGroup> findBySubjectType(SubjectType subjectType);
}
