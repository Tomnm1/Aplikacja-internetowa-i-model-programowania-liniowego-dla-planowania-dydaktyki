package pl.poznan.put.planner_endpoints.GeneratedPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.Plan.Plan;

import java.util.List;

/**
 * Interface to communicate with DB
 */
public interface GeneratedPlanRepository extends JpaRepository<GeneratedPlan, Integer> {
    List<GeneratedPlan> findAllByPlanOrderBySlotsDayAsc(Plan plan);
    List<GeneratedPlan> findAllByTeacherId(Integer teacherId);
    List<GeneratedPlan> findAllByClassroomClassroomID(Integer classroomID);
    List<GeneratedPlan> findAllByGroupId(Integer groupId);

}

