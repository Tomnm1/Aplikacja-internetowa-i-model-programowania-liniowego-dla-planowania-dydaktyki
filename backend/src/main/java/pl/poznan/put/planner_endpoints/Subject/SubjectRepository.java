package pl.poznan.put.planner_endpoints.Subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.poznan.put.planner_endpoints.Semester.Semester;

import java.util.List;

/**
 * Interface to communicate with DB
 */
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Subject findByNameAndSemester(String name, Semester semester);

    @Query(value = "SELECT *, check_classrooms(subject_id), check_groups(subject_id), check_teachers(subject_id) FROM subjects", nativeQuery = true)
    List<Object[]> getAllWithChecks();
}
