package pl.poznan.put.planner_endpoints.SubjectTypeGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;
import java.util.Set;

public interface SubjectTypeGroupRepository extends JpaRepository<SubjectTypeGroup, Integer> {
    @Transactional
    List<SubjectTypeGroup> findBySubjectType(SubjectType subjectType);

    @Query(value = "SELECT DISTINCT group_id FROM subject_types_groups", nativeQuery = true)
    Set<Integer> findAllAssignedGroups();

    boolean existsByGroupIdAndSubjectTypeSubjectTypeId(Integer group_id, Integer subjectType_subjectTypeId);
}
