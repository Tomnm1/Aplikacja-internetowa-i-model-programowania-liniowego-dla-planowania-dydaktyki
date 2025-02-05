package pl.poznan.put.or_planner.insert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.poznan.put.or_planner.Planner;
import pl.poznan.put.planner_endpoints.Classroom.ClassroomService;
import pl.poznan.put.planner_endpoints.GeneratedPlan.GeneratedPlan;
import pl.poznan.put.planner_endpoints.GeneratedPlan.GeneratedPlanService;
import pl.poznan.put.planner_endpoints.Group.GroupService;
import pl.poznan.put.planner_endpoints.Plan.Plan;
import pl.poznan.put.planner_endpoints.Plan.PlanService;
import pl.poznan.put.planner_endpoints.SlotsDay.SlotsDayService;
import pl.poznan.put.planner_endpoints.Subject.SubjectService;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectTypeService;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class InsertPlanToDbService {
    private final PlanService planService;
    private final GeneratedPlanService generatedPlanService;
    private final SlotsDayService slotsDayService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final SubjectTypeService subjectTypeService;
    private static final Logger logger = Logger.getLogger(Planner.class.getName());
    private final SubjectService subjectService;
    @Autowired
    InsertPlanToDbService(
          PlanService planService,
          GeneratedPlanService generatedPlanService,
          SlotsDayService slotsDayService,
          GroupService groupService,
          TeacherService teacherService,
          ClassroomService classroomService,
          SubjectTypeService subjectTypeService,
          SubjectService subjectService
    ){
        this.generatedPlanService = generatedPlanService;
        this.planService = planService;
        this.slotsDayService = slotsDayService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.subjectTypeService = subjectTypeService;
        this.subjectService = subjectService;
    }

    @Transactional
    public Plan insertSlots(List<PlannedSlot> plannedSlots, String planName){
        Plan plan = insertPlan(planName);
        logger.log(Level.INFO, "Schedule insert started");
        for(PlannedSlot plannedSlot: plannedSlots){
            GeneratedPlan generatedPlan = new GeneratedPlan();
            generatedPlan.plan = plan;
            generatedPlan.slotsDay = slotsDayService.getSlotsDayByID(plannedSlot.getSlotDayId()).get();
            generatedPlan.group = groupService.getGroupByID(plannedSlot.getGroupId()).get();
            generatedPlan.teacher = teacherService.getTeacherByID(plannedSlot.getTeacherId()).get();
            generatedPlan.classroom = classroomService.getRoomByID(plannedSlot.getClassroomId()).get();
            generatedPlan.subjectType = subjectTypeService.getsubjectTypeByID(plannedSlot.getSubjectTypeId()).get();
            generatedPlan.isEvenWeek = plannedSlot.getEvenWeek();
            generatedPlanService.createGeneratedPlan(generatedPlan);
        }
        logger.log(Level.INFO, "Schedule insert finished");
        return plan;
    }

    private Plan insertPlan(String planName){
        Plan plan = new Plan();
        LocalDateTime time = LocalDateTime.now();
        plan.name = planName;
        plan.creationDate = time;
        plan.published = false;
        planService.createPlan(plan);
        return plan;
    }
}
