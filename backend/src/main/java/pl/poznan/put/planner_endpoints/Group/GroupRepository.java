package pl.poznan.put.planner_endpoints.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.Semester.Semester;

/**
 * Interface to communicate with DB
 */
public interface GroupRepository  extends JpaRepository<Group, Integer> {
    Group findByCodeAndSemester(String code, Semester semester);
}
