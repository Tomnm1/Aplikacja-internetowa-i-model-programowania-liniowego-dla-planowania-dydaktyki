package pl.poznan.put.planner_endpoints.planner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poznan.put.or_planner.Planner;
import pl.poznan.put.or_planner.data.PlannerData;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.PlannerSubject;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.insert.InsertPlanToDbService;
import pl.poznan.put.or_planner.insert.PlanToExcelExportService;
import pl.poznan.put.or_planner.insert.PlannedSlot;
import pl.poznan.put.planner_endpoints.Plan.Plan;
import pl.poznan.put.planner_endpoints.planner.params.PlanningParams;
import pl.poznan.put.planner_endpoints.planner.service.ClassroomAssignmentService;
import pl.poznan.put.planner_endpoints.planner.service.PlanningDataAssemblingService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {
    private final InsertPlanToDbService insertPlanToDbService;
    private final PlanToExcelExportService planToExcelExportService;
    private final ClassroomAssignmentService classroomAssignmentService;
    private final PlanningDataAssemblingService planningDataAssemblingService;
    private final Planner planner;
    private static final Logger logger = Logger.getLogger(PlannerController.class.getName());

    @Autowired
    PlannerController(
            InsertPlanToDbService insertPlanToDbService,
            PlanToExcelExportService planToExcelExportService,
            ClassroomAssignmentService classroomAssignmentService,
            PlanningDataAssemblingService planningDataAssemblingService,
            Planner planner
    ){
        this.insertPlanToDbService = insertPlanToDbService;
        this.planToExcelExportService = planToExcelExportService;
        this.classroomAssignmentService = classroomAssignmentService;
        this.planningDataAssemblingService = planningDataAssemblingService;
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
            Map<String, Set<String>> subjectTypeToTeachers = plannerData.getSubjectTypeToTeachers();
            Map<String, Set<String>> groupToSubjectTypes = plannerData.getGroupToSubjectTypes();
            Map<String, Set<String>> classroomToSubjectTypes = plannerData.getClassroomToSubjectTypes();
            Map<String, Set<String>> teachersToSubjectTypes = plannerData.getTeachersToSubjectTypes();

            planner.initialize(groups, teachers, rooms, timeSlots, subjects, teachersLoad, subjectTypeToTeachers,
                    groupToSubjectTypes, classroomToSubjectTypes, teachersToSubjectTypes);

            List<PlannedSlot> optimizedSchedule = planner.optimizeSchedule();

            Plan plan = insertPlanToDbService.insertSlots(optimizedSchedule);

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
    public ResponseEntity<Void> startPlanningBasedOnDb(@RequestBody PlanningParams planningParams){
        logger.log(Level.INFO,"DataAssembling started");
//        PlannerData plannerData = planningDataAssemblingService.startAssembling("N", "zimowy"); // S/N oraz zimowy/letni
        PlannerData plannerData = planningDataAssemblingService.startAssembling(planningParams); // S/N oraz zimowy/letni
        logger.log(Level.INFO,"DataAssembling finished");
        try {
            List<String> groups = plannerData.getGroups();
            List<String> teachers = plannerData.getTeachers();
            List<String> rooms = plannerData.getRooms();
            List<String> timeSlots = plannerData.getTimeSlots();
            List<PlannerClassType> subjects = plannerData.getSubjects();
            List<TeacherLoad> teachersLoad = plannerData.getTeachersLoad();
            Map<String, Set<String>> teachersToSubjectTypes = plannerData.getTeachersToSubjectTypes();

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(plannerData);

            Map<String, Set<String>> subjectTypeToTeachers = plannerData.getSubjectTypeToTeachers();
            Map<String, Set<String>> groupToSubjectTypes = plannerData.getGroupToSubjectTypes();
            Map<String, Set<String>> classroomToSubjectTypes = plannerData.getClassroomToSubjectTypes();

            planner.initialize(groups, teachers, rooms, timeSlots, subjects, teachersLoad, subjectTypeToTeachers,
                    groupToSubjectTypes, classroomToSubjectTypes, teachersToSubjectTypes);

            List<PlannedSlot> optimizedSchedule = planner.optimizeSchedule();

            planner.cleanup();

            Plan plan = insertPlanToDbService.insertSlots(optimizedSchedule);

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

    public void printScheduleAsTable(List<String[]> schedule, List<String> groups, List<String> timeSlots) {
        int numGroups = groups.size();
        int numTimeSlots = timeSlots.size();

        System.out.println("Pierwszy slot - tydzień parzysty \nDrugi slot tydzień nieparzysty");
        System.out.print("    Time        |");
        for (int g = 0; g < numGroups; ++g) {
            System.out.print("-------" + groups.get(g) + "------|");
        }
        System.out.println();

        for (int t = 0; t < numTimeSlots; ++t) {
            // Drukowanie dla tygodnia parzystego
            System.out.print(timeSlots.get(t) + " (even) |");
            for (int g = 0; g < numGroups; ++g) {
                if (schedule.get(t * 2)[g] != null) {  // W harmonogramie parzysty tydzień ma indeksy 0, 2, 4, ...
                    System.out.print(schedule.get(t * 2)[g] + "|");
                } else {
                    System.out.print("---------------|");
                }
            }
            System.out.println();

            // Drukowanie dla tygodnia nieparzystego
            System.out.print(timeSlots.get(t) + " (odd)  |");
            for (int g = 0; g < numGroups; ++g) {
                if (schedule.get(t * 2 + 1)[g] != null) {  // W harmonogramie nieparzysty tydzień ma indeksy 1, 3, 5, ...
                    System.out.print(schedule.get(t * 2 + 1)[g] + "|");
                } else {
                    System.out.print("---------------|");
                }
            }
            System.out.println();
        }
    }
}
