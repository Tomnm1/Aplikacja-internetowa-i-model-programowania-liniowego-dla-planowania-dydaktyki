package pl.poznan.put.planner_endpoints.planner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poznan.put.or_planner.Planner;
import pl.poznan.put.or_planner.data.PlannerData;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.PlannerSubject;
import pl.poznan.put.or_planner.insert.InsertPlanToDbService;
import pl.poznan.put.or_planner.insert.PlanToExcelExportService;
import pl.poznan.put.or_planner.insert.PlannedSlot;
import pl.poznan.put.planner_endpoints.Plan.Plan;

import java.util.List;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {
    private final InsertPlanToDbService insertPlanToDbService;
    private final PlanToExcelExportService planToExcelExportService;

    @Autowired
    PlannerController(
            InsertPlanToDbService insertPlanToDbService,
            PlanToExcelExportService planToExcelExportService
    ){
        this.insertPlanToDbService = insertPlanToDbService;
        this.planToExcelExportService = planToExcelExportService;
    }

    @PostMapping("/start")
    public ResponseEntity<Void> startScheduling(@RequestBody PlannerData plannerData) {
        try {
            List<String> groups = plannerData.getGroups();
            List<String> teachers = plannerData.getTeachers();
            List<String> rooms = plannerData.getRooms();
            List<String> timeSlots = plannerData.getTimeSlots();
            List<PlannerClassType> subjects = plannerData.getSubjects();

            Planner planner = new Planner(groups, teachers, rooms, timeSlots, subjects);

            List<PlannedSlot> optimizedSchedule = planner.optimizeSchedule();

            Plan plan = insertPlanToDbService.insertSlots(optimizedSchedule);

            planToExcelExportService.exportPlanToExcel(plan);

//            printScheduleAsTable(optimizedSchedule, groups, timeSlots);
            System.out.println("done");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e);
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
