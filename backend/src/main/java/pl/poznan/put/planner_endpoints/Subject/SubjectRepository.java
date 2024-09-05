package pl.poznan.put.planner_endpoints.Subject;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}
