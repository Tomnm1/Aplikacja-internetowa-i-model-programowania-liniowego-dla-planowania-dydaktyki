package pl.poznan.put.or_planner;

import com.google.ortools.Loader;
import com.google.ortools.sat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Weź se wyloguj
 * if(Objects.equals(groups.get(g), "L1") && Objects.equals(subjects.get(p), "PTC"))
 *                                     System.out.println("wykluczam kombinację: " + groups.get(g) + " " + rooms.get(s) +
 *                                         " " + timeSlots.get(t) + " " + subjects.get(p) + " " + teachers.get(n));
 */
public class Planner {

    private final List<String> groups;
    private final List<String> subjects;
    private final List<String> rooms;
    private final List<String> timeSlots;
    private final Map<String, List<String>> roomToSubjects;
    private final Map<String, List<String>> subjectsToTeachers;
    private final List<String> teachers;
    private final Map<String, List<String>> groupsToSubjects;

    private final int numGroups;
    private final int numRooms;
    private final int numSubjects;
    private final int numTimeSlots;
    private final int numTeachers;

    public Planner(List<String> groups, List<String> subjects, List<String> rooms,
                   List<String> timeSlots, Map<String, List<String>> roomToSubjects,
                   Map<String, List<String>> subjectsToTeachers, List<String> teachers,
                   Map<String, List<String>> groupsToSubjects) {
        Loader.loadNativeLibraries();

        this.groups = groups;
        this.subjects = subjects;
        this.rooms = rooms;
        this.timeSlots = timeSlots;
        this.roomToSubjects = roomToSubjects;
        this.subjectsToTeachers = subjectsToTeachers;
        this.teachers = teachers;
        this.groupsToSubjects = groupsToSubjects;

        this.numGroups = groups.size();
        this.numSubjects = subjects.size();
        this.numRooms = rooms.size();
        this.numTimeSlots = timeSlots.size();
        this.numTeachers = teachers.size();
    }

    public List<String[]> optimizeSchedule() {
        CpModel model = new CpModel();

        // Variables - dana grupa g, w danej sali s, o danym czasie t, ma przedmiot p z nauczycielem n
        Literal[][][][][] x = new Literal[numGroups][numRooms][numTimeSlots][numSubjects][numTeachers];
        for (int g = 0; g < numGroups; ++g) {
            for (int s = 0; s < numRooms; ++s) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int p = 0; p < numSubjects; ++p) {
                        for (int n = 0; n < numTeachers; ++n){
                            x[g][s][t][p][n] = model.newBoolVar("x_" + groups.get(g) + "_" + rooms.get(s) + "_"
                                    + timeSlots.get(t) + "_" + subjects.get(p) + "_" + teachers.get(n));
                        }
                    }
                }
            }
        }

        // późniejsze wykluczanie jest skomplikowane (chyba). dlatego lepiej dodawać przypisania na wcześniejszym etapie, przy budowaniu ograniczeń. Update 05.10 chyba  jednak nie
        // Constraints
        // 1. Ograniczenie: każda sala może być zajęta tylko przez jedną grupę w danym czasie.
        System.out.println("Ograniczenie 1 <=1");
        for (int s = 0; s < numRooms; ++s) {
            for (int t = 0; t < numTimeSlots; ++t) {
                LinearExprBuilder roomConstraint = LinearExpr.newBuilder();
                for (int g = 0; g < numGroups; ++g) {
                    for (int p = 0; p < numSubjects; ++p) {
                        for (int n = 0; n < numTeachers; ++n) {
                            roomConstraint.addTerm(x[g][s][t][p][n], 1);
                        }
                    }
                }
                model.addLessOrEqual(roomConstraint, 1);
                System.out.println("Dodaję kombinację: " + rooms.get(s) + " " + timeSlots.get(t));
            }
        }

        // 2. Ograniczenie: każda grupa musi mieć określone przedmioty w jednym czasie i jednej sali.
        System.out.println("Ograniczenie 2 ==1");
        for (int g = 0; g < numGroups; ++g) {
            String group = groups.get(g);
            List<String> requiredSubjects = groupsToSubjects.get(group);
            for (int p = 0; p < numSubjects; ++p) {
                String subject = subjects.get(p);
                if(requiredSubjects.contains(subject)) {
                    List<Literal> groupSubjectConstraint = new ArrayList<>();
                    for (int s = 0; s < numRooms; ++s) {
                        for (int t = 0; t < numTimeSlots; ++t) {
                            for (int n = 0; n < numTeachers; ++n) {
                                groupSubjectConstraint.add(x[g][s][t][p][n]);
                            }
                        }
                    }
                    model.addExactlyOne(groupSubjectConstraint);
                }
            }
        }

        // 3. Ograniczenie: przedmioty mogą odbywać sie w określonych salach
        System.out.println("Ograniczenie 3");
        for (int s = 0; s < numRooms; ++s) {
            String room = rooms.get(s);
            List<String> allowedSubjects = roomToSubjects.get(room);
            for (int p = 0; p < numSubjects; ++p) {
                String subject = subjects.get(p);
                if (!allowedSubjects.contains(subject)) {
                    for (int t = 0; t < numTimeSlots; ++t) {
                        for (int g = 0; g < numGroups; ++g) {
                            for (int n = 0; n < numTeachers; ++n) {
                                model.addEquality(x[g][s][t][p][n], 0); // Ograniczenie wykluczające
                            }
                        }
                    }
                }
            }
        }

        // 4. Ograniczenie: nauczyciel może prowadzić tylko jeden przedmiot w jednym czasie.
        System.out.println("Ograniczenie 4 <= 1");
        for (int n = 0; n < numTeachers; ++n) {
            for (int t = 0; t < numTimeSlots; ++t) {
                LinearExprBuilder teacherConstraint = LinearExpr.newBuilder();
                for (int g = 0; g < numGroups; ++g) {
                    for (int s = 0; s < numRooms; ++s) {
                        for (int p = 0; p < numSubjects; ++p) {
                            teacherConstraint.addTerm(x[g][s][t][p][n], 1);
                        }
                    }
                }
                model.addLessOrEqual(teacherConstraint, 1);
            }
        }

        // 5. Ograniczenie: nauczyciel może prowadzić tylko przypisane mu przedmioty.
        System.out.println("Ograniczenie 5");
        for (int p = 0; p < numSubjects; ++p) {
            String subject = subjects.get(p);
            List<String> allowedTeachers = subjectsToTeachers.get(subject);
            for (int n = 0; n < numTeachers; ++n) {
                String teacher = teachers.get(n);
                if (!allowedTeachers.contains(teacher)) {
                    for (int g = 0; g < numGroups; ++g) {
                        for (int s = 0; s < numRooms; ++s) {
                            for (int t = 0; t < numTimeSlots; ++t) {
                                model.addEquality(x[g][s][t][p][n], 0); // Nauczyciel nie może prowadzić tego przedmiotu
                            }
                        }
                    }
                }
            }
        }

        // 6. Ograniczenie: dana grupa może mieć tylko jedne zajęcia w jednym slocie czasowym
        System.out.println("Ograniczenie 6: Grupa może mieć tylko jedne zajęcia w danym czasie.");
        for (int g = 0; g < numGroups; ++g) {
            for (int t = 0; t < numTimeSlots; ++t) {
                LinearExprBuilder groupConstraint = LinearExpr.newBuilder();
                for (int s = 0; s < numRooms; ++s) {
                    for (int p = 0; p < numSubjects; ++p) {
                        for (int n = 0; n < numTeachers; ++n) {
                            groupConstraint.addTerm(x[g][s][t][p][n], 1);
                        }
                    }
                }
                model.addLessOrEqual(groupConstraint, 1);
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
                        for (int n = 0; n < numTeachers; ++n) {
                            if (solver.booleanValue(x[g][s][t][p][n])) {
//                                System.out.println("Wiersz planu: " + groups.get(g) + " " + rooms.get(s) +
//                                        " " + timeSlots.get(t) + " " + subjects.get(p) + " " + teachers.get(n));
                                row[g] = "Room " + rooms.get(s) + " " + subjects.get(p) + " " + teachers.get(n);
                            }
                        }
                    }
                }
            }
            scheduleTable.add(row);
        }

        return scheduleTable;
    }
}
