package pl.poznan.put.planner_endpoints.planner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poznan.put.PlanningProgress.PlanningProgressService;
import pl.poznan.put.PlanningProgress.PlanningStatus;
import pl.poznan.put.or_planner.Planner;
import pl.poznan.put.or_planner.data.PlannerData;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.data.helpers.TeacherPreferences;
import pl.poznan.put.or_planner.insert.InsertPlanToDbService;
import pl.poznan.put.or_planner.insert.PlanToExcelExportService;
import pl.poznan.put.or_planner.insert.PlannedSlot;
import pl.poznan.put.planner_endpoints.Plan.Plan;
import pl.poznan.put.planner_endpoints.Teacher.Degree;
import pl.poznan.put.planner_endpoints.planner.params.PlanningParams;
import pl.poznan.put.planner_endpoints.planner.service.ClassroomAssignmentService;
import pl.poznan.put.planner_endpoints.planner.service.PlanningDataAssemblingService;
import pl.poznan.put.planner_endpoints.planner.service.PlanningDataValidationService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {
    private final InsertPlanToDbService insertPlanToDbService;
    private final PlanToExcelExportService planToExcelExportService;
    private final ClassroomAssignmentService classroomAssignmentService;
    private final PlanningDataAssemblingService planningDataAssemblingService;
    private final PlanningDataValidationService planningDataValidationService;
    private final PlanningProgressService planningProgressService;
    private final Planner planner;
    private static final Logger logger = Logger.getLogger(PlannerController.class.getName());

    @Autowired
    PlannerController(
            InsertPlanToDbService insertPlanToDbService,
            PlanToExcelExportService planToExcelExportService,
            ClassroomAssignmentService classroomAssignmentService,
            PlanningDataAssemblingService planningDataAssemblingService,
            PlanningDataValidationService planningDataValidationService,
            PlanningProgressService planningProgressService,
            Planner planner
    ){
        this.insertPlanToDbService = insertPlanToDbService;
        this.planToExcelExportService = planToExcelExportService;
        this.classroomAssignmentService = classroomAssignmentService;
        this.planningDataAssemblingService = planningDataAssemblingService;
        this.planningDataValidationService = planningDataValidationService;
        this.planningProgressService = planningProgressService;
        this.planner = planner;
    }

    @PostMapping("/start")
    public ResponseEntity<Void> startScheduling(@RequestBody PlannerData plannerData) {
        try {
            List<String> groups = plannerData.getGroups();
            List<String> teachers = plannerData.getTeachers();
            List<String> rooms = plannerData.getRooms();
            List<String> timeSlots = plannerData.getTimeSlots();
            List<PlannerClassType> subjects = plannerData.getSubjects();
            List<TeacherLoad> teachersLoad = plannerData.getTeachersLoad();
            List<TeacherPreferences> teacherPreferences = plannerData.getTeacherPreferences();
            Map<String, Set<String>> subjectTypeToTeachers = plannerData.getSubjectTypeToTeachers();
            Map<String, Set<String>> groupToSubjectTypes = plannerData.getGroupToSubjectTypes();
            Map<String, Set<String>> classroomToSubjectTypes = plannerData.getClassroomToSubjectTypes();
            Map<String, Set<String>> teachersToSubjectTypes = plannerData.getTeachersToSubjectTypes();
            Set<String> teachersWithPreferences = plannerData.getTeachersWithPreferences();
            Map<String, Degree> teacherToDegree = plannerData.getTeacherToDegree();

            planner.initialize(groups, teachers, rooms, timeSlots, subjects, teachersLoad, teacherPreferences, subjectTypeToTeachers,
                    groupToSubjectTypes, classroomToSubjectTypes, teachersToSubjectTypes, teachersWithPreferences, teacherToDegree);

            List<PlannedSlot> optimizedSchedule = planner.optimizeSchedule();

            //TODO: to nie jest używane na froncie to wpisałem cokolwiek żeby java mnie kochała
            Plan plan = insertPlanToDbService.insertSlots(optimizedSchedule, "NAZWA");

            planToExcelExportService.exportPlanToExcel(plan);

//            printScheduleAsTable(optimizedSchedule, groups, timeSlots);
            System.out.println("done");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/startPlanningBasedOnDb")
    public ResponseEntity<Map<String, String>> startPlanningBasedOnDb(@RequestBody PlanningParams planningParams) {
        logger.log(Level.INFO,"DataAssembling started");

        String jobId = UUID.randomUUID().toString();
        logger.log(Level.INFO,"Job id is {0}", jobId);
        planningProgressService.setProgress(jobId, 0, PlanningStatus.IN_PROGRESS);
        CompletableFuture.runAsync(() -> {
            try {
                PlannerData plannerData = planningDataAssemblingService.startAssembling(planningParams);
                logger.log(Level.INFO,"DataAssembling finished");
                logger.log(Level.INFO,"DataValidation started");

                planningDataValidationService.executeValidations(plannerData);
                logger.log(Level.INFO,"DataValidation finished");

                planningProgressService.setProgress(jobId, 30, PlanningStatus.IN_PROGRESS);

                List<String> groups = plannerData.getGroups();
                List<String> teachers = plannerData.getTeachers();
                List<String> rooms = plannerData.getRooms();
                List<String> timeSlots = plannerData.getTimeSlots();
                List<PlannerClassType> subjects = plannerData.getSubjects();
                List<TeacherLoad> teachersLoad = plannerData.getTeachersLoad();
                List<TeacherPreferences> teacherPreferences = plannerData.getTeacherPreferences();
                Map<String, Set<String>> subjectTypeToTeachers = plannerData.getSubjectTypeToTeachers();
                Map<String, Set<String>> groupToSubjectTypes = plannerData.getGroupToSubjectTypes();
                Map<String, Set<String>> classroomToSubjectTypes = plannerData.getClassroomToSubjectTypes();
                Map<String, Set<String>> teachersToSubjectTypes = plannerData.getTeachersToSubjectTypes();
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(plannerData);

                planningProgressService.setProgress(jobId, 50, PlanningStatus.IN_PROGRESS);
            planner.initialize(groups, teachers, rooms, timeSlots, subjects, teachersLoad, teacherPreferences,
                    subjectTypeToTeachers, groupToSubjectTypes, classroomToSubjectTypes, teachersToSubjectTypes,
                    teachersWithPreferences, teacherToDegree);

                List<PlannedSlot> optimizedSchedule = planner.optimizeSchedule();


                planningProgressService.setProgress(jobId, 80, PlanningStatus.IN_PROGRESS);

                planner.cleanup();

                Plan plan = insertPlanToDbService.insertSlots(optimizedSchedule, planningParams.getPlanName());

                planToExcelExportService.exportPlanToExcel(plan);

                planningProgressService.setProgress(jobId, 100, PlanningStatus.DONE);

                System.out.println("done");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
                planningProgressService.setProgress(jobId, 100, PlanningStatus.ERROR);
            }
        });

        Map<String, String> response = new HashMap<>();
        response.put("jobId", jobId);

        return ResponseEntity.accepted().body(response);
    }

    @PostMapping("/assignClassroomsBasedOnCapacity")
    public ResponseEntity<Void> assignClassroomsBasedOnCapacity(){
        try {
            classroomAssignmentService.assignClassroomsBasedOnCapacity();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
