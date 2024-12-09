package pl.poznan.put.planner_endpoints.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import pl.poznan.put.planner_endpoints.Semester.Semester;

/**
 * Interface to communicate with DB
 */
public interface GroupRepository  extends JpaRepository<Group, Integer> {
    Group findByCodeAndSemester(String code, Semester semester);

    @Query(value = "SELECT * FROM groups g WHERE g.semester_id = :id AND g.group_type = 'laboratoria' ORDER BY g.code", nativeQuery = true)
    List<Group> findBysemester_id(Integer id);
}
