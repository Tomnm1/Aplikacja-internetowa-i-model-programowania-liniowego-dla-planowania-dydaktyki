package pl.poznan.put.planner_endpoints.Subject;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.Semester.Semester;

/**
 * Interface to communicate with DB
 */
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    boolean existsByNameAndSemester(String name, Semester semester);
}
