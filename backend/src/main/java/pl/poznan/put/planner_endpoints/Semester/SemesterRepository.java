package pl.poznan.put.planner_endpoints.Semester;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;

/**
 * Interface to communicate with DB
 */
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    boolean existsByNumberAndSpecialisation(String number, Specialisation specialisation);
}
