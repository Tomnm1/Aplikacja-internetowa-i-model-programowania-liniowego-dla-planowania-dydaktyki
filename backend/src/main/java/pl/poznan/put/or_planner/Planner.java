package pl.poznan.put.or_planner;

import com.google.ortools.Loader;
import com.google.ortools.sat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Planner {

    private final ArrayList<String> groups;
    private final ArrayList<String> subjects;
    private final ArrayList<String> rooms;
    private final ArrayList<String> timeSlots;
    private final Map<String, List<String>> roomToSubjects;

    private final int numGroups;
    private final int numRooms;
    private final int numSubjects;
    private final int numTimeSlots;

    public Planner(ArrayList<String> groups, ArrayList<String> subjects, ArrayList<String> rooms,
                   ArrayList<String> timeSlots, Map<String, List<String>> roomToSubjects) {
        Loader.loadNativeLibraries();

        this.groups = groups;
        this.subjects = subjects;
        this.rooms = rooms;
        this.timeSlots = timeSlots;
        this.roomToSubjects = roomToSubjects;

        this.numGroups = groups.size();
        this.numSubjects = subjects.size();
        this.numRooms = rooms.size();
        this.numTimeSlots = timeSlots.size();
    }

    public List<String[]> optimizeSchedule() {
        CpModel model = new CpModel();

        // Variables - dana grupa g, w danej sali s, o danym czasie t, ma przedmiot p
        Literal[][][][] x = new Literal[numGroups][numRooms][numTimeSlots][numSubjects];
        for (int g = 0; g < numGroups; ++g) {
            for (int s = 0; s < numRooms; ++s) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int p = 0; p < numSubjects; ++p) {
                        x[g][s][t][p] = model.newBoolVar("x_" + g + "_" + s + "_" + t + "_" + p);
                    }
                }
            }
        }

        // Constraints
        // 1. Ograniczenie: każda sala może być zajęta tylko przez jedną grupę w danym czasie.
        for (int s = 0; s < numRooms; ++s) {
            for (int t = 0; t < numTimeSlots; ++t) {
                LinearExprBuilder roomConstraint = LinearExpr.newBuilder();
                for (int g = 0; g < numGroups; ++g) {
                    for (int p = 0; p < numSubjects; ++p) {
                        roomConstraint.addTerm(x[g][s][t][p], 1);
                    }
                }
                model.addLessOrEqual(roomConstraint, 1);
            }
        }

        // 2. Ograniczenie: każda grupa musi mieć każdy przedmiot w jednym czasie i jednej sali.
        for (int g = 0; g < numGroups; ++g) {
            for (int p = 0; p < numSubjects; ++p) {
                List<Literal> groupSubjectConstraint = new ArrayList<>();
                for (int s = 0; s < numRooms; ++s) {
                    for (int t = 0; t < numTimeSlots; ++t) {
                        groupSubjectConstraint.add(x[g][s][t][p]);
                    }
                }
                model.addExactlyOne(groupSubjectConstraint);
            }
        }

        // 3. Ograniczenie: przedmioty mogą odbywać sie w określonych salach
        for (int g = 0; g < numGroups; ++g) {
            for (int s = 0; s < numRooms; ++s) {
                String room = rooms.get(s);
                List<String> allowedSubjects = roomToSubjects.get(room);
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int p = 0; p < numSubjects; ++p) {
                        String subject = subjects.get(p);
                        if (!allowedSubjects.contains(subject)) {
                            model.addEquality(x[g][s][t][p], 0); //ograniczenie wykluczające
                        }
                    }
                }
            }
        }

        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(model);

        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
            System.out.println("Found a solution!");
        } else {
            System.out.println("No feasible solution found.");
            return null;
        }

        List<String[]> scheduleTable = new ArrayList<>();
        for (int t = 0; t < numTimeSlots; ++t) {
            String[]row = new String[numGroups];
            for (int g = 0; g < numGroups; ++g) {
                for (int s = 0; s < numRooms; ++s) {
                    for (int p = 0; p < numSubjects; ++p) {
                        if (solver.booleanValue(x[g][s][t][p])) {
                            row[g] = "Room " + rooms.get(s) + " " + subjects.get(p);
                        }
                    }
                }
            }
            scheduleTable.add(row);
        }

        return scheduleTable;
    }
}
