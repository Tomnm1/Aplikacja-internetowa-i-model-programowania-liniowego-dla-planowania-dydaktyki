package pl.poznan.put.planner_endpoints.SubjectTypeGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

public interface SubjectTypeGroupRepository extends JpaRepository<SubjectTypeGroup, Integer> {
    @Transactional
    List<SubjectTypeGroup> findBySubjectType(SubjectType subjectType);

    boolean existsByGroupIdAndSubjectTypeSubjectTypeId(Integer group_id, Integer subjectType_subjectTypeId);
}
