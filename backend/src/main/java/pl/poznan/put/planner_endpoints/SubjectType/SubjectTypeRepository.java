package pl.poznan.put.planner_endpoints.SubjectType;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.Subject.Subject;

/**
 * Interface to communicate with DB
 */
public interface SubjectTypeRepository extends JpaRepository<SubjectType, Integer> {
    SubjectType findSubjectTypeBySubjectAndType(Subject subject, ClassTypeOwn type);
}
