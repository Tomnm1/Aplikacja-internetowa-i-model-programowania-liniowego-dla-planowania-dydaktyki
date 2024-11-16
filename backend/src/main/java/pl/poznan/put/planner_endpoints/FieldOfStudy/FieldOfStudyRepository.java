package pl.poznan.put.planner_endpoints.FieldOfStudy;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface FieldOfStudyRepository extends JpaRepository<FieldOfStudy, Integer> {
    FieldOfStudy findByName(String name);
}
