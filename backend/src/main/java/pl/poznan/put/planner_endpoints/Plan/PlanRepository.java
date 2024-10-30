package pl.poznan.put.planner_endpoints.Plan;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface PlanRepository extends JpaRepository<Plan, Integer> {
}
