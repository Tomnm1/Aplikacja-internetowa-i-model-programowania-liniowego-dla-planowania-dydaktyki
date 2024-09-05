package pl.poznan.put.planner_endpoints.planner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.poznan.put.or_planner.Planner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

    @GetMapping("/start")
    public void startScheduling() {
        try {
            ArrayList<String> groups = new ArrayList<>(
                    List.of("L1", "L2", "L3", "L4")
            );
            ArrayList<String> subjects = new ArrayList<>(
                    List.of("PTC", "Prr", "Prz", "SK1", "ASK", "SBD")
            );
            ArrayList<String> rooms = new ArrayList<>(
                    List.of("142", "143")
            );

            Map<String, List<String>> roomToSubjects = new HashMap<>();
            roomToSubjects.put("142", List.of("PTC", "Prr", "Prz"));
            roomToSubjects.put("143", List.of("Prz", "SK1", "ASK", "SBD"));

            ArrayList<String> timeSlots = new ArrayList<>(
                    List.of("Pn 08:00", "Pn 09:45", "Pn 11:45",
                            "Wt 08:00", "Wt 09:45", "Wt 11:45",
                            "Śr 08:00", "Śr 09:45", "Śr 11:45",
                            "Cz 08:00", "Cz 09:45", "Cz 11:45",
                            "Pt 08:00", "Pt 09:45")
            );

            Planner planner = new Planner(groups, subjects, rooms, timeSlots, roomToSubjects);

            List<String[]> optimizedSchedule = planner.optimizeSchedule();

            printScheduleAsTable(optimizedSchedule, groups, timeSlots);
            System.out.println("done");
        } catch (Exception e) {
            System.out.println(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

    public void printScheduleAsTable(List<String[]> schedule, List<String> groups, List<String> timeSlots) {
        int numGroups = groups.size();
        int numTimeSlots = timeSlots.size();

        // Print header
        System.out.print("    Time |");
        for (int g = 0; g < numGroups; ++g) {
            System.out.print("------" + groups.get(g) + "----|");
        }
        System.out.println();

        // Print table content
        for (int t = 0; t < numTimeSlots; ++t) {
            System.out.print(timeSlots.get(t) + " |");
            for (int g = 0; g < numGroups; ++g) {
                if (schedule.get(t)[g] != null) {
                    System.out.print(schedule.get(t)[g] + "|");
                } else {
                    System.out.print("------------|");
                }
            }
            System.out.println();
        }
    }
}
