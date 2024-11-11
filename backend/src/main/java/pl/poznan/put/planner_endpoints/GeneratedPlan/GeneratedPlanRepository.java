package pl.poznan.put.planner_endpoints.GeneratedPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.Plan.Plan;

import java.util.List;

/**
 * Interface to communicate with DB
 */
public interface GeneratedPlanRepository extends JpaRepository<GeneratedPlan, Integer> {
    List<GeneratedPlan> findAllByPlanOrderBySlotsDayAsc(Plan plan);
}
