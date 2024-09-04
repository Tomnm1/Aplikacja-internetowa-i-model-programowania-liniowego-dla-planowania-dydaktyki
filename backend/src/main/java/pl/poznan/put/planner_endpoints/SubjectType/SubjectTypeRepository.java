package pl.poznan.put.planner_endpoints.SubjectType;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface SubjectTypeRepository extends JpaRepository<SubjectType, Integer> {
}
