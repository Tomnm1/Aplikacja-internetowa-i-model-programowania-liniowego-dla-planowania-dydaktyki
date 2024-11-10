package pl.poznan.put.planner_endpoints.Classroom;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.Building.Building;

/**
 * Interface to communicate with DB
 */
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    boolean existsByCodeAndBuilding(String code, Building building);
}
