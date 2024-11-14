package pl.poznan.put.or_planner;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import pl.poznan.put.or_planner.constraints.ConstraintsManager;
import pl.poznan.put.or_planner.data.helpers.PlannerSubject;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.insert.PlannedSlot;

import java.util.*;

/**
 * Weź se wyloguj
 * if(Objects.equals(groups.get(g), "L1") && Objects.equals(subjects.get(p), "PTC"))
 *                   System.out.println("wykluczam kombinację: " + groups.get(g) + " " + rooms.get(s) +
 *                   " " + timeSlots.get(t) + " " + subjects.get(p) + " " + teachers.get(n)) + " " + typesOfClasses.get(c);
 */
public class Planner {

    private final List<String> groups;
    private final List<String> teachers;
    private final List<String> rooms;
    private final List<String> timeSlots;
    private final List<PlannerSubject> subjects;
    private final List<TeacherLoad> teacherLoadList;

    private final int numGroups;
    private final int numRooms;
    private final int numSubjects;
    private final int numTimeSlots;
    private final int numTeachers;

    private final List<String> evenTimeSlots;
    private final List<String> oddTimeSlots;


    public Planner(List<String> groups, List<String> teachers, List<String> rooms, List<String> timeSlots,
                   List<PlannerSubject> subjects, List<TeacherLoad> teacherLoadList) {
        Loader.loadNativeLibraries();

        this.groups = groups;
        this.teachers = teachers;
        this.rooms = rooms;
        this.timeSlots = timeSlots;
        this.subjects = subjects;
        this.teacherLoadList = teacherLoadList;

        this.numGroups = groups.size();
        this.numSubjects = subjects.size();
        this.numRooms = rooms.size();
        this.numTimeSlots = timeSlots.size();
        this.numTeachers = teachers.size();

        this.evenTimeSlots = new ArrayList<>();
        this.oddTimeSlots = new ArrayList<>();

        for (String slot : timeSlots) {
            evenTimeSlots.add(slot + "_even");
            oddTimeSlots.add(slot + "_odd");
        }

    }

    public List<PlannedSlot> optimizeSchedule() {
        MPSolver solver = MPSolver.createSolver("SCIP");
        MPObjective objective = solver.objective();
        objective.setMinimization();

        // Variables - dana grupa g, w danej sali s, o danym czasie t, ma przedmiot p z nauczycielem n
        MPVariable[][][][][] xEven =
                new MPVariable[numGroups][numRooms][evenTimeSlots.size()][numSubjects][numTeachers];
        MPVariable[][][][][] xOdd =
                new MPVariable[numGroups][numRooms][oddTimeSlots.size()][numSubjects][numTeachers];
        for (int g = 0; g < numGroups; ++g) {
            for (int s = 0; s < numRooms; ++s) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int p = 0; p < numSubjects; ++p) {
                        for (int n = 0; n < numTeachers; ++n){
                            xEven[g][s][t][p][n] = solver.makeBoolVar("xEven_" + groups.get(g) + "_"
                                    + rooms.get(s) + "_" + timeSlots.get(t) + "_" + subjects.get(p) + "_"
                                    + teachers.get(n));
                            xOdd[g][s][t][p][n] = solver.makeBoolVar("xOdd_" + groups.get(g) + "_"
                                    + rooms.get(s) + "_" + timeSlots.get(t) + "_" + subjects.get(p) + "_"
                                    + teachers.get(n));
                        }
                    }
                }
            }
        }

        ConstraintsManager constraintsManager = new ConstraintsManager(solver, groups, teachers, rooms, timeSlots,
                subjects, teacherLoadList);

        constraintsManager.addRoomOccupationConstraint(xEven, xOdd);

        constraintsManager.assignSubjectsToGroupsAndBlockGroupsConstraint(xEven, xOdd);

        constraintsManager.oneTeacherOneClassConstraint(xEven, xOdd);

        constraintsManager.oneGroupOneClassConstraint(xEven, xOdd);

        constraintsManager.teachersLoadConstraint(xEven, xOdd);


        MPSolver.ResultStatus status = solver.solve();

        if (status == MPSolver.ResultStatus.OPTIMAL || status == MPSolver.ResultStatus.FEASIBLE) {
            System.out.println("Found a " + status + " solution!");
        } else {
            System.out.println("No feasible solution found.");
            return null;
        }

        List<PlannedSlot> scheduleTable = new ArrayList<>();
        for (int t = 0; t < numTimeSlots; ++t) {
            for (int g = 0; g < numGroups; ++g) {
                for (int s = 0; s < numRooms; ++s) {
                    for (int p = 0; p < numSubjects; ++p) {
                        for (int n = 0; n < numTeachers; ++n) {
                            if (xEven[g][s][t][p][n].solutionValue() == 1) {
                                PlannedSlot evenSlot = new PlannedSlot(timeSlots.get(t), groups.get(g), teachers.get(n),
                                        rooms.get(s), subjects.get(p).getName(), true);
                                scheduleTable.add(evenSlot);
                            }
                            if (xOdd[g][s][t][p][n].solutionValue() == 1) {
                                PlannedSlot oddSlot = new PlannedSlot(timeSlots.get(t), groups.get(g), teachers.get(n),
                                        rooms.get(s), subjects.get(p).getName(), false);
                                scheduleTable.add(oddSlot);
                            }
                        }
                    }
                }
            }
        }

        return scheduleTable;
    }
}

// fiat voluntas tua