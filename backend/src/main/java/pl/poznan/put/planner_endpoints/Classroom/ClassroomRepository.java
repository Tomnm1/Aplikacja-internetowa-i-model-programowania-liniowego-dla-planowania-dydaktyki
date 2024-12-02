package pl.poznan.put.planner_endpoints.Classroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.poznan.put.planner_endpoints.Building.Building;

import java.util.List;

/**
 * Interface to communicate with DB
 */
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    boolean existsByCodeAndBuilding(String code, Building building);

    @Query(value = "SELECT * FROM classrooms WHERE capacity >= :capacity ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
    List<Classroom> findRandomClassroomWithCapacity(@Param("capacity") int capacity);

    List<Classroom> findClassroomsByBuilding(Building building);
}
