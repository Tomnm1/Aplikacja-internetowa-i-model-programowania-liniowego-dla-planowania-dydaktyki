package pl.poznan.put.planner_endpoints.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
