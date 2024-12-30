package pl.poznan.put.planner_endpoints.GeneratedPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.Plan.Plan;

import java.util.List;

/**
 * Interface to communicate with DB
 */
public interface GeneratedPlanRepository extends JpaRepository<GeneratedPlan, Integer> {
    List<GeneratedPlan> findAllByPlanOrderBySlotsDayAsc(Plan plan);
    List<GeneratedPlan> findAllByTeacherIdAndPlan_PlanId(Integer teacherId, Integer planPlanId);
    List<GeneratedPlan> findAllByClassroomClassroomIDAndPlan_PlanId(Integer classroomID, Integer planPlanId);
    List<GeneratedPlan> findAllByGroupSemesterSemesterIdAndPlan_PlanId(Integer groupSemesterId, Integer planPlanId);
    List<GeneratedPlan> findAllByPlan_PlanId(Integer planPlanId);
}

