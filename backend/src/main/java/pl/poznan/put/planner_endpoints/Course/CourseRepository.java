package pl.poznan.put.planner_endpoints.Course;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface CourseRepository  extends JpaRepository<Course, Integer> {
}
