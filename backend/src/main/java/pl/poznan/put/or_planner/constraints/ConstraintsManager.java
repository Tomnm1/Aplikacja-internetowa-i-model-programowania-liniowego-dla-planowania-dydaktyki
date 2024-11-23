package pl.poznan.put.or_planner.constraints;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static pl.poznan.put.constans.Constans.Weeks.*;

public class ConstraintsManager {
    private final MPSolver solver;
    private final List<String> groups;
    private final List<String> teachers;
    private final List<String> rooms;
    private final List<String> timeSlots;
    private final List<PlannerClassType> subjects;

    private final int numGroups;
    private final int numRooms;
    private final int numSubjects;
    private final int numTimeSlots;
    private final int numTeachers;

    private final List<String> evenTimeSlots;
    private final List<String> oddTimeSlots;


    public ConstraintsManager(MPSolver solver, List<String> groups, List<String> teachers, List<String> rooms, List<String> timeSlots,
                              List<PlannerClassType> subjects) {
        this.solver = solver;
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

    public void addRoomOccupationConstraint(MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd) {
        for (int s = 0; s < numRooms; ++s) {
            for (int t = 0; t < numTimeSlots; ++t) {
                ConstraintBuilder roomOccupationConstraintEven = new ConstraintBuilder(
                        solver, "RoomOccupiedEvenWeek_" + s + "_" + t, 0, 1);
                ConstraintBuilder roomOccupationConstraintOdd = new ConstraintBuilder(
                        solver, "RoomOccupiedOddWeek_" + s + "_" + t, 0, 1);

                iterateAllGroupsSubjectsTeachers(roomOccupationConstraintEven, roomOccupationConstraintOdd, xEven, xOdd,
                        1, t, s, WEEKLY);

            }
        }
    }

    public void assignSubjectsToGroupsAndBlockGroupsConstraint(MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd){
        for (int p = 0; p < numSubjects; ++p) {
            String frequency = subjects.get(p).getFrequency();
            List<String> classRooms = subjects.get(p).getRooms();
            List<String> classTeachers = subjects.get(p).getTeachers();
            Map<String, List<String>> groupMappings = subjects.get(p).getGroupMappings();
            Set<String> assignedGroups = groupMappings.keySet();

            List<Integer> roomIndices = classRooms.stream()
                    .map(rooms::indexOf)
                    .toList();

            List<Integer> teacherIndices = classTeachers.stream()
                    .map(teachers::indexOf)
                    .toList();

            List<Integer> assignedGroupsIndices = assignedGroups.stream()
                    .map(groups::indexOf)
                    .toList();
            for (int assignedGroupIndex : assignedGroupsIndices) {
                assignSubjectToGroupConstraint(xEven, xOdd, roomIndices, teacherIndices, assignedGroupIndex, p, frequency);

                List<String> blockedGroups = groupMappings.get(groups.get(assignedGroupIndex));
                List<Integer> blockedGroupsIndices = blockedGroups.stream()
                        .map(groups::indexOf)
                        .toList();
                blockGroupsConstraint(xEven, xOdd, blockedGroupsIndices, assignedGroupIndex, p, frequency);

                groupsMustHaveAllRequiredSubjectsConstraint(xEven, xOdd, roomIndices, teacherIndices,
                        assignedGroupIndex, p, frequency);
            }
        }
    }

    private void groupsMustHaveAllRequiredSubjectsConstraint(MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd,
                                                             List<Integer> roomIndices, List<Integer> teacherIndices,
                                                             int assignedGroup, int subject, String frequency){
        if (frequency.equals(WEEKLY)) {
            ConstraintBuilder subjectConstraintEven = new ConstraintBuilder(solver, "SubjectConstraintEven_" + assignedGroup + "_" + subject, 1, 1);
            ConstraintBuilder subjectConstraintOdd = new ConstraintBuilder(solver, "SubjectConstraintOdd_" + assignedGroup + "_" + subject, 1, 1);

            for (int room : roomIndices) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int teacher : teacherIndices) {
                        subjectConstraintEven.setCoefficient(xEven[assignedGroup][room][t][subject][teacher], 1);
                        subjectConstraintOdd.setCoefficient(xOdd[assignedGroup][room][t][subject][teacher], 1);
                    }
                }
            }

        } else if (frequency.equals(EVEN_WEEKS)) {
            ConstraintBuilder subjectConstraintEven = new ConstraintBuilder(solver, "SubjectConstraintEven_" + assignedGroup + "_" + subject, 1, 1);

            for (int room : roomIndices) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int teacher : teacherIndices) {
                        subjectConstraintEven.setCoefficient(xEven[assignedGroup][room][t][subject][teacher], 1);
                    }
                }
            }

        } else if (frequency.equals(ODD_WEEKS)) {
            ConstraintBuilder subjectConstraintOdd = new ConstraintBuilder(solver, "SubjectConstraintOdd_" + assignedGroup + "_" + subject, 1, 1);

            for (int room : roomIndices) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int teacher : teacherIndices) {
                        subjectConstraintOdd.setCoefficient(xOdd[assignedGroup][room][t][subject][teacher], 1);
                    }
                }
            }
        }
    }

    private void assignSubjectToGroupConstraint(MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd,
                                                List<Integer> roomIndices, List<Integer> teacherIndices,
                                                int assignedGroupIndex, int subject, String frequency){
        for (int roomIndex : roomIndices) {
            for (int teacherIndex : teacherIndices) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    ConstraintBuilder assignedGroupConstraintEven = new ConstraintBuilder(solver, "Assigned group even week: " + assignedGroupIndex + "_subject_" + subject, 0, 1);
                    ConstraintBuilder assignedGroupConstraintOdd = new ConstraintBuilder(solver, "Assigned group odd week: " + assignedGroupIndex + "_subject_" + subject, 0, 1);

                    handleWeeks(assignedGroupConstraintEven, assignedGroupConstraintOdd, xEven, xOdd, frequency,
                            1, 1, assignedGroupIndex, roomIndex, t, subject, teacherIndex);


                    if (frequency.equals(WEEKLY)){
                        ConstraintBuilder sameSlotConstraint = new ConstraintBuilder(solver, "Same slot constraint for both weeks " + assignedGroupIndex + "_" + t, 0, 0);
                        sameSlotConstraint.setCoefficient(xEven[assignedGroupIndex][roomIndex][t][subject][teacherIndex], 1);
                        sameSlotConstraint.setCoefficient(xOdd[assignedGroupIndex][roomIndex][t][subject][teacherIndex], -1);
                    }
                }
            }
        }
    }

    private void blockGroupsConstraint(MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd, List<Integer> blockedGroupsIndices,
                                       int assignedGroupIndex, int subject, String frequency){
        for (int t = 0; t < numTimeSlots; ++t) {
            for (int roomIndex = 0; roomIndex < numRooms; ++roomIndex) {
                for (int teacherIndex = 0; teacherIndex < numTeachers; ++teacherIndex) {

                    for (int blockedGroupIndex : blockedGroupsIndices) {
                        ConstraintBuilder blockGroupsEven = new ConstraintBuilder(solver, "Even Group " + assignedGroupIndex + " blocks groups " + blockedGroupIndex, 0, 1);
                        blockGroupsEven.setCoefficient(xEven[assignedGroupIndex][roomIndex][t][subject][teacherIndex], 1);
                        ConstraintBuilder blockGroupsOdd = new ConstraintBuilder(solver, "Odd Group " + assignedGroupIndex + " blocks groups " + blockedGroupIndex, 0, 1);
                        blockGroupsOdd.setCoefficient(xOdd[assignedGroupIndex][roomIndex][t][subject][teacherIndex], 1);

                        iterateAllRoomsTeachersSubjects(blockGroupsEven, blockGroupsOdd, xEven, xOdd, 1, blockedGroupIndex, t, frequency);
                    }
                }
            }
        }
    }

    public void oneTeacherOneClassConstraint(MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd){
        for (int teacher = 0; teacher < numTeachers; ++teacher) {
            for (int time = 0; time < numTimeSlots; ++time) {
                ConstraintBuilder teacherEvenConstraint = new ConstraintBuilder(solver, "One teacher constraintEven " + teacher ,0, 1);
                ConstraintBuilder teacherOddConstraint = new ConstraintBuilder(solver, "One teacher constraintOdd " + teacher ,0, 1);

                iterateAllRoomsSubjectsGroups(teacherEvenConstraint, teacherOddConstraint, xEven, xOdd, 1,
                        teacher, time, WEEKLY);
            }
        }
    }

    public void oneGroupOneClassConstraint(MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd){
        for (int group = 0; group < numGroups; ++group) {
            for (int time = 0; time < numTimeSlots; ++time) {
                ConstraintBuilder groupEvenConstraint = new ConstraintBuilder(solver, "GroupEvenConstraint_" + group, 0, 1);
                ConstraintBuilder groupOddConstraint = new ConstraintBuilder(solver, "GroupOddConstraint_" + group, 0, 1);

                iterateAllRoomsTeachersSubjects(groupEvenConstraint, groupOddConstraint, xEven, xOdd, 1, group, time, WEEKLY);
            }
        }
    }

    private void handleWeeks(ConstraintBuilder evenConstraint, ConstraintBuilder oddConstraint,
                             MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd, String frequency,
                             int evenCoefficient, int oddCoefficient,
                             int group, int room, int time, int subject, int teacher){
        if (frequency.equals(WEEKLY) || frequency.equals(EVEN_WEEKS)) {
            evenConstraint.setCoefficient(xEven[group][room][time][subject][teacher], evenCoefficient);
        }
        if (frequency.equals(WEEKLY) || frequency.equals(ODD_WEEKS)) {
            oddConstraint.setCoefficient(xOdd[group][room][time][subject][teacher], oddCoefficient);
        }
    }

    private void iterateAllGroupsSubjectsTeachers(ConstraintBuilder evenConstraint, ConstraintBuilder oddConstraint,
                                                  MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd, int coefficient,
                                                  int time, int room, String frequency){
        for (int g = 0; g < numGroups; ++g) {
            for (int p = 0; p < numSubjects; ++p) {
                for (int n = 0; n < numTeachers; ++n) {
                    handleWeeks(evenConstraint, oddConstraint, xEven, xOdd, frequency, coefficient, coefficient,
                            g, room, time, p, n);
                }
            }
        }
    }

    private void iterateAllRoomsTeachersSubjects(ConstraintBuilder evenConstraint, ConstraintBuilder oddConstraint,
                                                 MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd, int coefficient,
                                                 int group, int time, String frequency){
        for (int room = 0; room < numRooms; ++room){
            for (int teacher = 0; teacher < numTeachers; ++teacher){
                for (int subject = 0; subject < numSubjects; ++subject){
                    handleWeeks(evenConstraint, oddConstraint, xEven, xOdd, frequency, coefficient, coefficient,
                            group, room, time, subject, teacher);
                }
            }
        }
    }

    private void iterateAllRoomsSubjectsGroups(ConstraintBuilder evenConstraint, ConstraintBuilder oddConstraint,
                                               MPVariable[][][][][] xEven, MPVariable[][][][][] xOdd, int coefficient,
                                               int teacher, int time, String frequency) {
        for (int room = 0; room < numRooms; room++){
            for (int subject = 0; subject < numSubjects; ++subject){
                for (int group = 0; group < numGroups; ++group){
                    handleWeeks(evenConstraint, oddConstraint, xEven, xOdd, frequency, coefficient, coefficient,
                            group, room, time, subject, teacher);
                }
            }
        }
    }
}
