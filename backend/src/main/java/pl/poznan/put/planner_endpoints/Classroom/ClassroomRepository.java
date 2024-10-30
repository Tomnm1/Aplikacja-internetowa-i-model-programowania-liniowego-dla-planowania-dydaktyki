package pl.poznan.put.planner_endpoints.Classroom;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
}
