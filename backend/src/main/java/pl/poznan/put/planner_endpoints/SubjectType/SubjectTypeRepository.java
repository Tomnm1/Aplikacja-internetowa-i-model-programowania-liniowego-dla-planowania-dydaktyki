package pl.poznan.put.planner_endpoints.SubjectType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.poznan.put.planner_endpoints.Subject.Subject;

import java.util.List;

/**
 * Interface to communicate with DB
 */
public interface SubjectTypeRepository extends JpaRepository<SubjectType, Integer> {
    SubjectType findSubjectTypeBySubjectAndType(Subject subject, ClassTypeOwn type);

    @Query("SELECT st FROM SubjectType st WHERE st.subject.SubjectId = :subjectId")
    List<SubjectType> findSubjectTypesBySubjectSubjectId(Integer subjectId);

    List<SubjectType> findAllBySubject(Subject subject);
}
