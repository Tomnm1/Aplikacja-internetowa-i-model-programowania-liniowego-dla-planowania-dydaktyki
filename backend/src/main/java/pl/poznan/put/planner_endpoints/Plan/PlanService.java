package pl.poznan.put.planner_endpoints.Plan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for plans
 */
@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    /**
     * return all plans
     * @return list of all plan objects
     */
    public List<Plan> getAllPlans() {return planRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));}

    /**
     * Finds room by ID
     * @param planId ID of plan
     * @return Optional - empty or with Plan
     */
    public Optional<Plan> getPlanByID(Integer planId) {return planRepository.findById(planId);}

    /**
     * Creates a plan
     * @param plan plan object to be inserted into DB
     * @return saved plan
     */
    public Plan createPlan(Plan plan) {return planRepository.save(plan);}

    /**
     * Updates a plan by given ID and params
     * @param planId id of plan to update
     * @param planParams params to update
     * @return Optional - null or with updated plan
     */
    public Plan updatePlanByID(Integer planId, Plan planParams){
        Optional<Plan> plan = planRepository.findById(planId);
        if (plan.isPresent()){
            Plan oldPlan = plan.get();
            oldPlan.name = planParams.name;
            oldPlan.creationDate = planParams.creationDate;
            return planRepository.save(oldPlan);
        } else {
            return null;
        }
    }

    /**
     * Deletes plan by ID
     * @param planId ID of plan
     */
    public void deletePlanByID(Integer planId){
        planRepository.deleteById(planId);
    }

    /**
     * Deletes all plans
     */
    public void deleteAllPlans(){
        planRepository.deleteAll();
    }
}
