package pl.poznan.put.planner_endpoints.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Interface to communicate with DB
 */
public interface GroupRepository  extends JpaRepository<Group, Integer> {
    @Query(value = "SELECT * FROM groups g WHERE g.semester_id = :id AND g.group_type = 'laboratoria'", nativeQuery = true)
    List<Group> findBysemester_id(Integer id);
}
