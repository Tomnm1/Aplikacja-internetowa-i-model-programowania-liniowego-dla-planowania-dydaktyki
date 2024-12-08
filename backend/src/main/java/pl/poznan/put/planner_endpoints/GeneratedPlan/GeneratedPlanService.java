package pl.poznan.put.planner_endpoints.GeneratedPlan;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Plan.Plan;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for generatedPlans
 */
@Service
public class GeneratedPlanService {
    @Autowired
    private GeneratedPlanRepository generatedPlanRepository;

    /**
     * return all generatedPlans
     * @return list of all generatedPlan objects
     */
    public List<GeneratedPlan> getAllGeneratedPlans() {return generatedPlanRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));}

    @Transactional
    public List<GeneratedPlanDTO> getAllGeneratedPlansDTO(){
        return generatedPlanRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream().map(GeneratedPlan::convertToDTO).toList();
    }

    @Transactional
    public List<GeneratedPlanDTO> getGeneratedPlansByTeacherId(Integer teacherId) {
        return generatedPlanRepository.findAllByTeacherId(teacherId)
                .stream()
                .map(GeneratedPlan::convertToDTO)
                .toList();
    }
    @Transactional
    public List<GeneratedPlanDTO> getGeneratedPlansByClassroomId(Integer classroomID) {
        return generatedPlanRepository.findAllByClassroomClassroomID (classroomID)
                .stream()
                .map(GeneratedPlan::convertToDTO)
                .toList();
    }
    @Transactional
    public List<GeneratedPlanDTO> findAllByGroupSemesterSemesterId(Integer semesterId) {
        return generatedPlanRepository.findAllByGroupSemesterSemesterId (semesterId)
                .stream()
                .map(GeneratedPlan::convertToDTO)
                .toList();
    }


    /**
     * Finds room by ID
     * @param generatedPlanId ID of generatedPlan
     * @return Optional - empty or with GeneratedPlan
     */
    public Optional<GeneratedPlan> getGeneratedPlanByID(Integer generatedPlanId) {return generatedPlanRepository.findById(generatedPlanId);}

    public List<GeneratedPlan> getGeneratedPlansByPlanId(Plan plan){
        return generatedPlanRepository.findAllByPlanOrderBySlotsDayAsc(plan);
    }

    /**
     * Creates a generatedPlan
     * @param generatedPlan generatedPlan object to be inserted into DB
     * @return saved generatedPlan
     */
    public GeneratedPlan createGeneratedPlan(GeneratedPlan generatedPlan) {return generatedPlanRepository.save(generatedPlan);}

    /**
     * Updates a generatedPlan by given ID and params
     * @param generatedPlanId id of generatedPlan to update
     * @param generatedPlanParams params to update
     * @return Optional - null or with updated generatedPlan
     */
    public GeneratedPlan updateGeneratedPlanByID(Integer generatedPlanId, GeneratedPlan generatedPlanParams){
        Optional<GeneratedPlan> generatedPlan = generatedPlanRepository.findById(generatedPlanId);
        if (generatedPlan.isPresent()){
            GeneratedPlan oldGeneratedPlan = generatedPlan.get();
            oldGeneratedPlan.plan = generatedPlanParams.plan;
            oldGeneratedPlan.slotsDay = generatedPlanParams.slotsDay;
            oldGeneratedPlan.group = generatedPlanParams.group;
            oldGeneratedPlan.teacher = generatedPlanParams.teacher;
            oldGeneratedPlan.classroom = generatedPlanParams.classroom;
            oldGeneratedPlan.subjectType = generatedPlanParams.subjectType;
            oldGeneratedPlan.isEvenWeek = generatedPlanParams.isEvenWeek;
            return generatedPlanRepository.save(oldGeneratedPlan);
        } else {
            return null;
        }
    }

    /**
     * Deletes generatedPlan by ID
     * @param generatedPlanId ID of generatedPlan
     */
    public void deleteGeneratedPlanByID(Integer generatedPlanId){
        generatedPlanRepository.deleteById(generatedPlanId);
    }

    /**
     * Deletes all generatedPlans
     */
    public void deleteAllGeneratedPlans(){
        generatedPlanRepository.deleteAll();
    }
}
