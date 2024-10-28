package pl.poznan.put.or_planner;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.PlannerSubject;

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

    private final int numGroups;
    private final int numRooms;
    private final int numSubjects;
    private final int numTimeSlots;
    private final int numTeachers;

    private final List<String> evenTimeSlots;  // Tygodnie parzyste
    private final List<String> oddTimeSlots;   // Tygodnie nieparzyste


    public Planner(List<String> groups, List<String> teachers, List<String> rooms, List<String> timeSlots,
                   List<PlannerSubject> subjects) {
        Loader.loadNativeLibraries();

        this.groups = groups;
        this.teachers = teachers;
        this.rooms = rooms;
        this.timeSlots = timeSlots;
        this.subjects = subjects;

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

    public List<String[]> optimizeSchedule() {
        MPSolver solver = MPSolver.createSolver("GLOP");
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

        // późniejsze wykluczanie jest skomplikowane (chyba). dlatego lepiej dodawać przypisania na wcześniejszym etapie, przy budowaniu ograniczeń. Update 05.10 chyba  jednak nie
        // Constraints
        // 1. Ograniczenie: każda sala może być zajęta tylko przez jedną grupę w danym czasie.
        //todo: tu trzeba to ogarnąć tak, że może być w danym czasie przede wszystkim jeden typ zajęć, a potem określać ile grup można tam walnąć
        for (int s = 0; s < numRooms; ++s) {
            for (int t = 0; t < numTimeSlots; ++t) {
                MPConstraint roomOccupationConstraintEven = solver.makeConstraint(0, 1, "Room occupied even week");
                MPConstraint roomOccupationConstraintOdd = solver.makeConstraint(0, 1, "Room occupied odd week");

                for (int g = 0; g < numGroups; ++g) {
                    for(int p = 0; p < numSubjects; ++p) {
                        for (int n = 0; n < numTeachers; ++n) {
                            roomOccupationConstraintEven.setCoefficient(xEven[g][s][t][p][n], 1);
                            roomOccupationConstraintOdd.setCoefficient(xOdd[g][s][t][p][n], 1);
                        }
                    }
                }
            }
        }

        // 2. Ograniczenie: każda grupa musi mieć określone przedmioty w jednym czasie i jednej sali.
        //todo: porobić metodę hadle type (ogólnie rozbić wszystko na metody) i potem standardowo obsłużyć cyklicznosć
        System.out.println("Ograniczenie 2: przypisywanie zajęć dla assignedGroups i blokowanie slotów dla blockedGroups");
        for (int p = 0; p < numSubjects; ++p) {
            PlannerSubject subject = subjects.get(p);
            List<PlannerClassType> classTypes = subject.getTypesOfClasses();

            for (PlannerClassType classType : classTypes) {
                String frequency = classType.getFrequency();
                List<String> classRooms = classType.getRooms();  // Dozwolone sale
                List<String> classTeachers = classType.getTeachers();  // Prowadzący
                Map<String, List<String>> groupMappings = classType.getGroupMappings();
                Set<String> assignedGroups = groupMappings.keySet();  // Grupy przypisane do zajęć

                // Znajdź indeksy dla grup, sal i nauczycieli
                List<Integer> roomIndices = classRooms.stream()
                        .map(rooms::indexOf)
                        .toList();

                List<Integer> teacherIndices = classTeachers.stream()
                        .map(teachers::indexOf)
                        .toList();

                List<Integer> assignedGroupsIndices = assignedGroups.stream()
                        .map(groups::indexOf)
                        .toList();

                // Przypisywanie zajęć dla grup z assignedGroups
                for (int assignedGroupIndex : assignedGroupsIndices) {
                    for (int roomIndex : roomIndices) {
                        for (int teacherIndex : teacherIndices) {
                            for (int t = 0; t < numTimeSlots; ++t) {
                                if (frequency.equals("weekly")) {
                                    // Sprawdzanie, czy slot jest zajęty przez assignedGroups
                                    MPConstraint assignedGroupConstraintEven = solver.makeConstraint(0, 1, "Assigned group even week " + assignedGroupIndex + "_slot_" + t);
                                    MPConstraint assignedGroupConstraintOdd = solver.makeConstraint(0, 1, "Assigned group odd week " + assignedGroupIndex + "_slot_" + t);

                                    assignedGroupConstraintEven.setCoefficient(xEven[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                    assignedGroupConstraintOdd.setCoefficient(xOdd[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);

                                    MPConstraint sameSlotConstraint = solver.makeConstraint(0, 0, "Same slot even and odd week " + assignedGroupIndex + "_" + t);
                                    sameSlotConstraint.setCoefficient(xEven[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                    sameSlotConstraint.setCoefficient(xOdd[assignedGroupIndex][roomIndex][t][p][teacherIndex], -1);
                                } else if (frequency.equals("even_weeks")) {
                                    // Zajęcia odbywają się tylko w tygodniach parzystych
                                    MPConstraint assignedGroupConstraintEven = solver.makeConstraint(0, 1, "Assigned group even week " + assignedGroupIndex + "_slot_" + t);

                                    assignedGroupConstraintEven.setCoefficient(xEven[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);

                                } else if (frequency.equals("odd_weeks")) {
                                    // Zajęcia odbywają się tylko w tygodniach nieparzystych
                                    MPConstraint assignedGroupConstraintOdd = solver.makeConstraint(0, 1, "Assigned group odd week " + assignedGroupIndex + "_slot_" + t);

                                    assignedGroupConstraintOdd.setCoefficient(xOdd[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);

                                }
                            }
                        }
                    }
                }
            }
        }

        // 2a. Ograniczenie: Punkty karne, gdy przedmiot odbywa się w tym samym czasie dla grupy z listy zalokowanej
        for (int p = 0; p < numSubjects; ++p) {
            PlannerSubject subject = subjects.get(p);
            List<PlannerClassType> classTypes = subject.getTypesOfClasses();

            for (PlannerClassType classType : classTypes) {
                String frequency = classType.getFrequency();
                Map<String, List<String>> groupMappings = classType.getGroupMappings();
                Set<String> assignedGroups = groupMappings.keySet();

                List<Integer> assignedGroupsIndices = assignedGroups.stream()
                        .map(groups::indexOf)
                        .toList();

                for (int assignedGroupIndex : assignedGroupsIndices) {
                    List<String> blockedGroups = groupMappings.get(groups.get(assignedGroupIndex));
                    List<Integer> blockedGroupsIndices = blockedGroups.stream()
                            .map(groups::indexOf)
                            .toList();

                    for (int t = 0; t < numTimeSlots; ++t) {
                        for (int roomIndex = 0; roomIndex < numRooms; ++roomIndex) {
                            for (int teacherIndex = 0; teacherIndex < numTeachers; ++teacherIndex) {
                                for (int blockedGroupIndex : blockedGroupsIndices) {

                                    double penaltyWeight = classType.getType().equals("lecture") ? 1000 : 10; // dynamiczna waga

                                    MPVariable penaltyVar = solver.makeNumVar(0, 1,
                                            "Penalty_var_" + assignedGroupIndex + "_" + blockedGroupIndex + "_t_" + t);

                                    if (frequency.equals("weekly")) {
                                        MPConstraint softConstraintEven = solver.makeConstraint(0, 1,
                                                "Soft_constraint_even_" + assignedGroupIndex + "_blocked_" + blockedGroupIndex + "_t_" + t);
                                        MPConstraint softConstraintOdd = solver.makeConstraint(0, 1,
                                                "Soft_constraint_odd_" + assignedGroupIndex + "_blocked_" + blockedGroupIndex + "_t_" + t);

                                        softConstraintEven.setCoefficient(xEven[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                        softConstraintEven.setCoefficient(xEven[blockedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                        softConstraintEven.setCoefficient(penaltyVar, 1);

                                        softConstraintOdd.setCoefficient(xOdd[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                        softConstraintOdd.setCoefficient(xOdd[blockedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                        softConstraintOdd.setCoefficient(penaltyVar, 1);

                                    } else if (frequency.equals("even_weeks")) {
                                        MPConstraint softConstraintEven = solver.makeConstraint(0, 1,
                                                "Soft_constraint_even_" + assignedGroupIndex + "_blocked_" + blockedGroupIndex + "_t_" + t);

                                        softConstraintEven.setCoefficient(xEven[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                        softConstraintEven.setCoefficient(xEven[blockedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                        softConstraintEven.setCoefficient(penaltyVar, 1);

                                    } else if (frequency.equals("odd_weeks")) {
                                        MPConstraint softConstraintOdd = solver.makeConstraint(0, 1,
                                                "Soft_constraint_odd_" + assignedGroupIndex + "_blocked_" + blockedGroupIndex + "_t_" + t);

                                        softConstraintOdd.setCoefficient(xOdd[assignedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                        softConstraintOdd.setCoefficient(xOdd[blockedGroupIndex][roomIndex][t][p][teacherIndex], 1);
                                        softConstraintOdd.setCoefficient(penaltyVar, 1);
                                    }

                                    // Dodaj zmienną kary do funkcji celu z dynamicznie ustawioną wagą
                                    objective.setCoefficient(penaltyVar, penaltyWeight);
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Ograniczenie: nauczyciel może prowadzić tylko jeden przedmiot w jednym czasie.
        //todo: tutaj typ będzie miał wpływ chyba na to co sie dzieje (czytaj: ile zajęć może mieć typ w jednym czasie)
        System.out.println("Ograniczenie 3 - nauczyciel prowadzi jedne zajęcia w danym slocie");
        for (int n = 0; n < numTeachers; ++n) {
            for (int t = 0; t < numTimeSlots; ++t) {
                MPConstraint teacherEvenConstraint = solver.makeConstraint(0, 1, "TeacherEvenConstraint_" + n + "_" + t);
                MPConstraint teacherOddConstraint = solver.makeConstraint(0, 1, "TeacherOddConstraint_" + n + "_" + t);

                for (int s = 0; s < numRooms; ++s) {
                    for (int p = 0; p < numSubjects; ++p) {
                        for (int g = 0; g < numGroups; ++g) {
                            teacherEvenConstraint.setCoefficient(xEven[g][s][t][p][n], 1);
                            teacherOddConstraint.setCoefficient(xOdd[g][s][t][p][n], 1);
                        }
                    }
                }
            }
        }

        // 4. Ograniczenie: dana grupa może mieć tylko jedne zajęcia w jednym slocie czasowym
        //todo: To będzie raczej łatwa rozbudowa, po prostu nowy wymiar i elo.
        System.out.println("Ograniczenie 4: Grupa może mieć tylko jedne zajęcia w danym czasie.");
        for (int g = 0; g < numGroups; ++g) {
            for (int t = 0; t < numTimeSlots; ++t) {
                // Tworzymy ograniczenie
                MPConstraint groupEvenConstraint = solver.makeConstraint(0, 1, "GroupEvenConstraint_" + g + "_" + t);
                MPConstraint groupOddConstraint = solver.makeConstraint(0, 1, "GroupOddConstraint_" + g + "_" + t);

                // Dodajemy zmienne do ograniczenia
                for (int s = 0; s < numRooms; ++s) {
                    for (int p = 0; p < numSubjects; ++p) {
                        for (int n = 0; n < numTeachers; ++n) {
                            groupEvenConstraint.setCoefficient(xEven[g][s][t][p][n], 1);
                            groupOddConstraint.setCoefficient(xOdd[g][s][t][p][n], 1);
                        }
                    }
                }
                System.out.println("Zajęcia w układzie: " + groups.get(g) +
                        " " + timeSlots.get(t));
            }
        }

        // Ograniczenie 5: Każda grupa musi mieć wszystkie wymagane przedmioty.
        System.out.println("Ograniczenie 5: Każda grupa z assignedGroups musi mieć wszystkie wymagane przedmioty.");
        for (int p = 0; p < numSubjects; ++p) {
            PlannerSubject subject = subjects.get(p);
            List<PlannerClassType> classTypes = subject.getTypesOfClasses();

            for (PlannerClassType classType : classTypes) {
                String frequency = classType.getFrequency();  // "weekly", "even_weeks", "odd_weeks"
                List<String> classRooms = classType.getRooms();  // Dozwolone sale
                List<String> classTeachers = classType.getTeachers();  // Prowadzący
                Set<String> assignedGroups = classType.getGroupMappings().keySet();  // Grupy przypisane do zajęć

                // Znajdź indeksy dla grup, sal i nauczycieli
                List<Integer> roomIndices = classRooms.stream()
                        .map(rooms::indexOf)
                        .toList();

                List<Integer> teacherIndices = classTeachers.stream()
                        .map(teachers::indexOf)
                        .toList();

                // Znajdź indeksy dla grup assignedGroups
                List<Integer> assignedGroupIndices = assignedGroups.stream()
                        .map(groups::indexOf)
                        .toList();

                // Iteracja po grupach
                for (int group : assignedGroupIndices) {
                    if (frequency.equals("weekly")) {
                        // Zajęcia odbywają się w tygodniach parzystych i nieparzystych (even + odd)
                        MPConstraint subjectConstraintEven = solver.makeConstraint(1, 1, "SubjectConstraintEven_" + group + "_" + p);
                        MPConstraint subjectConstraintOdd = solver.makeConstraint(1, 1, "SubjectConstraintOdd_" + group + "_" + p);

                        for (int room : roomIndices) {
                            for (int t = 0; t < numTimeSlots; ++t) {
                                for (int teacher : teacherIndices) {
                                    subjectConstraintEven.setCoefficient(xEven[group][room][t][p][teacher], 1);
                                    subjectConstraintOdd.setCoefficient(xOdd[group][room][t][p][teacher], 1);
                                }
                            }
                        }

                    } else if (frequency.equals("even_weeks")) {
                        // Zajęcia odbywają się tylko w tygodniach parzystych
                        MPConstraint subjectConstraintEven = solver.makeConstraint(1, 1, "SubjectConstraintEven_" + group + "_" + p);

                        for (int room : roomIndices) {
                            for (int t = 0; t < numTimeSlots; ++t) {
                                for (int teacher : teacherIndices) {
                                    subjectConstraintEven.setCoefficient(xEven[group][room][t][p][teacher], 1);
                                }
                            }
                        }

                    } else if (frequency.equals("odd_weeks")) {
                        // Zajęcia odbywają się tylko w tygodniach nieparzystych
                        MPConstraint subjectConstraintOdd = solver.makeConstraint(1, 1, "SubjectConstraintOdd_" + group + "_" + p);

                        for (int room : roomIndices) {
                            for (int t = 0; t < numTimeSlots; ++t) {
                                for (int teacher : teacherIndices) {
                                    subjectConstraintOdd.setCoefficient(xOdd[group][room][t][p][teacher], 1);
                                }
                            }
                        }
                    }

                    // Informacja dla każdej grupy
                    System.out.println("Grupa z assignedGroups musi mieć: " + groups.get(group) +
                            " " + subjects.get(p).getName() + " (" + frequency + ")");
                }
            }
        }



        MPSolver.ResultStatus status = solver.solve();

        if (status == MPSolver.ResultStatus.OPTIMAL || status == MPSolver.ResultStatus.FEASIBLE) {
            System.out.println("Found a " + status + " solution!");
        } else {
            System.out.println("No feasible solution found.");
            return null;
        }

        List<String[]> scheduleTable = new ArrayList<>();
        for (int t = 0; t < numTimeSlots; ++t) {
            String[] rowEven = new String[numGroups];
            String[] rowOdd = new String[numGroups];

            for (int g = 0; g < numGroups; ++g) {
                for (int s = 0; s < numRooms; ++s) {
                    for (int p = 0; p < numSubjects; ++p) {
                        for (int n = 0; n < numTeachers; ++n) {
                            if (xEven[g][s][t][p][n].solutionValue() > 0) {
                                rowEven[g] = "Room " + rooms.get(s) + " " + subjects.get(p).getName()
                                        + " " + teachers.get(n);
                            }
                            if (xOdd[g][s][t][p][n].solutionValue() > 0) {
                                rowOdd[g] = "Room " + rooms.get(s) + " " + subjects.get(p).getName()
                                        + " " + teachers.get(n);
                            }
                        }
                    }
                }
            }

            scheduleTable.add(rowEven);
            scheduleTable.add(rowOdd);
        }

        return scheduleTable;
    }
}

// fiat voluntas tua