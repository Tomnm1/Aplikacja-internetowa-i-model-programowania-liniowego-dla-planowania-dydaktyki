package pl.poznan.put.or_planner.constraints;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.or_planner.data.helpers.GroupListService;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.data.helpers.TeacherLoadSubject;

import java.util.*;

import static pl.poznan.put.constans.Constants.Weeks.*;

@Service
public class ConstraintsManager {
    private MPSolver solver;
    private List<String> groups;
    private List<String> teachers;
    private List<String> rooms;
    private List<String> timeSlots;
    private List<PlannerClassType> subjects;
    private List<TeacherLoad> teacherLoadList;
    private List<String> subjectNames;
    private int numGroups;
    private int numRooms;
    private int numSubjects;
    private int numTimeSlots;
    private int numTeachers;

    private List<String> evenTimeSlots;
    private List<String> oddTimeSlots;
    private final GroupListService groupListService;
    private Map<String, Set<String>> teachersToSubjectTypes;
    private Map<String, Set<String>> subjectTypeToTeachers;
    private Map<String, Set<String>> groupToSubjectTypes;
    private Map<String, Set<String>> classroomToSubjectTypes;
    private Set<String> teachersWithPreferences;

    @Autowired
    ConstraintsManager(
            GroupListService groupListService
    ){
        this.groupListService = groupListService;
    }


    public void initialize(MPSolver solver, List<String> groups, List<String> teachers, List<String> rooms, List<String> timeSlots,
                              List<PlannerClassType> subjects, List<TeacherLoad> teacherLoadList,
                           Map<String, Set<String>> subjectTypeToTeachers, Map<String, Set<String>> groupToSubjectTypes,
                           Map<String, Set<String>> classroomToSubjectTypes, Map<String, Set<String>> teachersToSubjectTypes,
                           Set<String> teachersWithPreferences) {
        this.solver = solver;
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

        this.subjectTypeToTeachers = subjectTypeToTeachers;
        this.groupToSubjectTypes = groupToSubjectTypes;
        this.classroomToSubjectTypes = classroomToSubjectTypes;
        this.teachersToSubjectTypes = teachersToSubjectTypes;
        this.teachersWithPreferences = teachersWithPreferences;

        for (String slot : timeSlots) {
            evenTimeSlots.add(slot + "_even");
            oddTimeSlots.add(slot + "_odd");
        }

        this.subjectNames = new ArrayList<>();
        for(PlannerClassType subject: this.subjects){
            this.subjectNames.add(subject.getId());
        }
    }

    public void addRoomOccupationConstraint(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap) {
        for (int s = 0; s < numRooms; ++s) {
            for (int t = 0; t < numTimeSlots; ++t) {
                ConstraintBuilder roomOccupationConstraintEven = new ConstraintBuilder(
                        solver, "RoomOccupiedEvenWeek_" + s + "_" + t,  1);
                ConstraintBuilder roomOccupationConstraintOdd = new ConstraintBuilder(
                        solver, "RoomOccupiedOddWeek_" + s + "_" + t, 1);

                iterateAllGroupsSubjectsTeachers(roomOccupationConstraintEven, roomOccupationConstraintOdd, xEvenMap, xOddMap,
                        1, t, s, WEEKLY);

            }
        }
    }

    public void assignSubjectsToGroupsAndBlockGroupsConstraint(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap){
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
                assignSubjectToGroupConstraint(xEvenMap, xOddMap, roomIndices, teacherIndices, assignedGroupIndex, p, frequency);

                List<String> blockedGroups = groupMappings.get(groups.get(assignedGroupIndex));
                List<Integer> blockedGroupsIndices = blockedGroups.stream()
                        .map(groups::indexOf)
                        .toList();
                if(!blockedGroupsIndices.isEmpty())
                    blockGroupsConstraint(xEvenMap, xOddMap, blockedGroupsIndices, assignedGroupIndex, p, frequency);

                groupsMustHaveAllRequiredSubjectsConstraint(xEvenMap, xOddMap, roomIndices, teacherIndices,
                        assignedGroupIndex, p, frequency);
            }
        }
    }

    private void groupsMustHaveAllRequiredSubjectsConstraint(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap,
                                                             List<Integer> roomIndices, List<Integer> teacherIndices,
                                                             int assignedGroup, int subject, String frequency){
        if (frequency.equals(WEEKLY)) {
            ConstraintBuilder subjectConstraintEven = new ConstraintBuilder(solver, "SubjectConstraintEven_" + assignedGroup + "_" + subject, 1, 1);
            ConstraintBuilder subjectConstraintOdd = new ConstraintBuilder(solver, "SubjectConstraintOdd_" + assignedGroup + "_" + subject, 1, 1);

            for (int room : roomIndices) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int teacher : teacherIndices) {
                        String varNameEven = "xEven_" + assignedGroup + "_" + room + "_" + t + "_" + subject + "_" + teacher;
                        String varNameOdd = "xOdd_" + assignedGroup + "_" + room + "_" + t + "_" + subject + "_" + teacher;
                        subjectConstraintEven.setCoefficient(xEvenMap.get(varNameEven), 1);
                        subjectConstraintOdd.setCoefficient(xOddMap.get(varNameOdd), 1);
                    }
                }
            }

        } else if (frequency.equals(EVEN_WEEKS)) {
            ConstraintBuilder subjectConstraintEven = new ConstraintBuilder(solver, "SubjectConstraintEven_" + assignedGroup + "_" + subject, 1, 1);

            for (int room : roomIndices) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int teacher : teacherIndices) {
                        String varNameEven = "xEven_" + assignedGroup + "_" + room + "_" + t + "_" + subject + "_" + teacher;
                        subjectConstraintEven.setCoefficient(xEvenMap.get(varNameEven), 1);
                    }
                }
            }

        } else if (frequency.equals(ODD_WEEKS)) {
            ConstraintBuilder subjectConstraintOdd = new ConstraintBuilder(solver, "SubjectConstraintOdd_" + assignedGroup + "_" + subject, 1, 1);

            for (int room : roomIndices) {
                for (int t = 0; t < numTimeSlots; ++t) {
                    for (int teacher : teacherIndices) {
                        String varNameOdd = "xOdd_" + assignedGroup + "_" + room + "_" + t + "_" + subject + "_" + teacher;
                        subjectConstraintOdd.setCoefficient(xOddMap.get(varNameOdd), 1);
                    }
                }
            }
        }
    }

    //Raczej nie trzeba upraszczać
    private void assignSubjectToGroupConstraint(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap,
                                                List<Integer> roomIndices, List<Integer> teacherIndices,
                                                int assignedGroupIndex, int subject, String frequency){
        int evenUpperBound = 1;
        int oddUpperBound = 1;
        if (frequency.equals(EVEN_WEEKS)) {
            oddUpperBound = 0;
        }
        if (frequency.equals(ODD_WEEKS)) {
            evenUpperBound = 0;
        }

        ConstraintBuilder assignedGroupConstraintEven = new ConstraintBuilder(solver, "Assigned group even week: " + groups.get(assignedGroupIndex) + "_subject_" + subjects.get(subject).getId(),  evenUpperBound);
        ConstraintBuilder assignedGroupConstraintOdd = new ConstraintBuilder(solver, "Assigned group odd week: " + groups.get(assignedGroupIndex) + "_subject_" + subjects.get(subject).getId(),  oddUpperBound);

        for (int roomIndex : roomIndices) {
            for (int teacherIndex : teacherIndices) {
                for (int t = 0; t < numTimeSlots; ++t) {

                    String varNameEven = "xEven_" + assignedGroupIndex + "_" + roomIndex + "_" + t + "_" + subject + "_" + teacherIndex;
                    String varNameOdd = "xOdd_" + assignedGroupIndex + "_" + roomIndex + "_" + t + "_" + subject + "_" + teacherIndex;
                    MPVariable evenVar = xEvenMap.get(varNameEven);
                    assignedGroupConstraintEven.setCoefficient(evenVar, 1);
                    MPVariable oddVar = xOddMap.get(varNameOdd);
                    assignedGroupConstraintOdd.setCoefficient(oddVar, 1);

//                    handleWeeks(assignedGroupConstraintEven, assignedGroupConstraintOdd, xEvenMap, xOddMap, frequency,
//                            1, 1, assignedGroupIndex, roomIndex, t, subject, teacherIndex);

                    if (frequency.equals(WEEKLY)){
                        ConstraintBuilder sameSlotConstraint = new ConstraintBuilder(solver, "Same slot constraint for both weeks " + assignedGroupIndex + "_" + t, 0, 0);
                        sameSlotConstraint.setCoefficient(xEvenMap.get(varNameEven), 1);
                        sameSlotConstraint.setCoefficient(xOddMap.get(varNameOdd), -1);
                    }
                }
            }
        }
    }

    private void blockGroupsConstraint(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap,
                                       List<Integer> blockedGroupsIndices, int assignedGroupIndex, int subject,
                                       String frequency){
        String subjectTypeId = subjects.get(subject).getId();
        for (int t = 0; t < numTimeSlots; ++t) {
            for (int roomIndex = 0; roomIndex < numRooms; ++roomIndex) {
                if(!classroomToSubjectTypes.get(rooms.get(roomIndex)).contains(subjectTypeId))
                    continue;
                for (int teacherIndex = 0; teacherIndex < numTeachers; ++teacherIndex) {
                    if(!subjects.get(subject).getTeachers().contains(teachers.get(teacherIndex)) || !teachersToSubjectTypes.get(teachers.get(teacherIndex)).contains(subjectTypeId))
                        continue;
                    for (int blockedGroupIndex : blockedGroupsIndices) {
                        if(!groupToSubjectTypes.get(groups.get(blockedGroupIndex)).contains(subjectTypeId))
                            continue;
                        String varNameEven = "xEven_" + assignedGroupIndex + "_" + roomIndex + "_" + t + "_" + subject + "_" + teacherIndex;
                        String varNameOdd = "xOdd_" + assignedGroupIndex + "_" + roomIndex + "_" + t + "_" + subject + "_" + teacherIndex;
                        ConstraintBuilder blockGroupsEven = null;
                        ConstraintBuilder blockGroupsOdd = null;
                        if(frequency.equals(WEEKLY) || frequency.equals(EVEN_WEEKS)) {
                            blockGroupsEven = new ConstraintBuilder(solver, "Even Group " + assignedGroupIndex + " blocks groups " + blockedGroupIndex, 1);
                            blockGroupsEven.setCoefficient(xEvenMap.get(varNameEven), 1);
                        }
                        if(frequency.equals(WEEKLY) || frequency.equals(ODD_WEEKS)) {
                            blockGroupsOdd = new ConstraintBuilder(solver, "Odd Group " + assignedGroupIndex + " blocks groups " + blockedGroupIndex, 1);
                            blockGroupsOdd.setCoefficient(xOddMap.get(varNameOdd), 1);
                        }

                        iterateAllRoomsTeachersSubjects(blockGroupsEven, blockGroupsOdd, xEvenMap, xOddMap, 1, blockedGroupIndex, t, frequency);
                    }
                }
            }
        }
    }

    public void oneTeacherOneClassConstraint(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap){
        for (int teacher = 0; teacher < numTeachers; ++teacher) {
            for (int time = 0; time < numTimeSlots; ++time) {
                ConstraintBuilder teacherEvenConstraint = new ConstraintBuilder(solver, "One teacher constraintEven " + teacher , 1);
                ConstraintBuilder teacherOddConstraint = new ConstraintBuilder(solver, "One teacher constraintOdd " + teacher , 1);

                iterateAllRoomsSubjectsGroups(teacherEvenConstraint, teacherOddConstraint, xEvenMap, xOddMap, 1,
                        teacher, time, WEEKLY);
            }
        }
    }

    public void oneGroupOneClassConstraint(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap){
        for (int group = 0; group < numGroups; ++group) {
            for (int time = 0; time < numTimeSlots; ++time) {
                ConstraintBuilder groupEvenConstraint = new ConstraintBuilder(solver, "GroupEvenConstraint_" + group, 1);
                ConstraintBuilder groupOddConstraint = new ConstraintBuilder(solver, "GroupOddConstraint_" + group, 1);

                iterateAllRoomsTeachersSubjects(groupEvenConstraint, groupOddConstraint, xEvenMap, xOddMap, 1, group, time, WEEKLY);
            }
        }
    }

    public void teachersLoadConstraint(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap){
        for(TeacherLoad teacherLoad: this.teacherLoadList){
            String teacherId = teacherLoad.getTeacher();
            if(!teachersToSubjectTypes.containsKey(teacherId))
                continue;
            int teacherIndex = this.teachers.indexOf(teacherLoad.getTeacher());
            for(TeacherLoadSubject teacherLoadSubject: teacherLoad.getTeacherLoadSubjectList()){
                String subjectName = teacherLoadSubject.getName();
                int subjectIndex = this.subjectNames.indexOf(subjectName);
                PlannerClassType plannerClassType = this.subjects.get(subjectIndex);
                List<String> allGroups = groupListService.getGroupsFromGroupMapping(plannerClassType.getGroupMappings());

                int maxGroups = Integer.parseInt(teacherLoadSubject.getMaxGroups());
                List<String> groups = teacherLoadSubject.getGroups();
                List<Integer> groupsIndices = groups.stream()
                        .map(this.groups::indexOf)
                        .toList();

                ConstraintBuilder teacherLoadEvenConstraint = new ConstraintBuilder(solver, "teacherLoadEvenConstraint", maxGroups);
                ConstraintBuilder teacherLoadOddConstraint = new ConstraintBuilder(solver, "teacherLoadEvenConstraint", maxGroups);
                for(int group: groupsIndices){
                    if(!allGroups.contains(this.groups.get(group)))
                        continue;
                    for(int s = 0; s < numRooms; ++s){
                        if(!classroomToSubjectTypes.get(rooms.get(s)).contains(subjectName))
                            continue;
                        for(int t = 0; t < numTimeSlots; ++t){
                            String varNameEven = "xEven_" + group + "_" + s + "_" + t + "_" + subjectIndex + "_" + teacherIndex;
                            String varNameOdd = "xOdd_" + group + "_" + s + "_" + t + "_" + subjectIndex + "_" + teacherIndex;
                            teacherLoadOddConstraint.setCoefficient(xOddMap.get(varNameOdd), 1);
                            teacherLoadEvenConstraint.setCoefficient(xEvenMap.get(varNameEven), 1);
                        }
                    }
                }
            }
        }
    }

    public void aggregateTeacherTimeVariables(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap,
                                              Map<String, MPVariable> teacherToTimeEvenVarsMap,
                                              Map<String, MPVariable> teacherToTimeOddVarsMap){
        for (int n = 0; n < numTeachers; n++) {
            if(!teachersWithPreferences.contains(teachers.get(n)))
                continue;
            for (int t = 0; t < numTimeSlots; t++) {
                MPVariable teacherTimeEven = teacherToTimeEvenVarsMap.get("teacherTimeEven_" + n + "_" + t);
                MPVariable teacherTimeOdd = teacherToTimeEvenVarsMap.get("teacherTimeOdd_" + n + "_" + t);
                // Dolne ograniczenia: teacherTime ≥ suma zmiennych x
                ConstraintBuilder evenLowerBoundConstraint = new ConstraintBuilder(solver, "teacherTimeEven_LB_" + n + "_" + t, -Double.MAX_VALUE, 0);
                ConstraintBuilder oddLowerBoundConstraint = new ConstraintBuilder(solver, "teacherTimeOdd_LB_" + n + "_" + t, -Double.MAX_VALUE, 0);

                // Górne ograniczenia: teacherTime ≤ suma zmiennych x
                ConstraintBuilder evenUpperBoundConstraint = new ConstraintBuilder(solver, "teacherTimeEven_UB_" + n + "_" + t, 0);
                ConstraintBuilder oddUpperBoundConstraint = new ConstraintBuilder(solver, "teacherTimeOdd_UB_" + n + "_" + t, 0);

                // Dodanie ograniczeń z iteracją po wszystkich kombinacjach
                for (int p = 0; p < numSubjects; p++) {
                    if(!subjectTypeToTeachers.get(subjects.get(p).getId()).contains(teachers.get(n)))
                        continue;
                    for (int g = 0; g < numGroups; g++) {
                        if(!groupToSubjectTypes.get(groups.get(g)).contains(subjects.get(p).getId()))
                            continue;
                        for (int s = 0; s < numRooms; s++) {
                            if(classroomToSubjectTypes.get(rooms.get(s)).contains(subjects.get(p).getId()))
                                continue;
                            // Pobierz zmienne decyzyjne
                            MPVariable xEven = xEvenMap.get("xEven_" + g + "_" + s + "_" + t + "_" + p + "_" + n);
                            MPVariable xOdd = xOddMap.get("xOdd_" + g + "_" + s + "_" + t + "_" + p + "_" + n);

                            // Dodanie terminów xEven i xOdd do odpowiednich ograniczeń
                            evenLowerBoundConstraint.setCoefficient(xEven, -1.0);
                            oddLowerBoundConstraint.setCoefficient(xOdd, -1.0);

                            evenUpperBoundConstraint.setCoefficient(xEven, 1.0);
                            oddUpperBoundConstraint.setCoefficient(xOdd, 1.0);
                        }
                    }
                }

                // Związanie teacherTimeEven i teacherTimeOdd z sumami zmiennych x
                evenLowerBoundConstraint.setCoefficient(teacherTimeEven, 1.0); // TeacherTime >= suma(xEven)
                oddLowerBoundConstraint.setCoefficient(teacherTimeOdd, 1.0);

                evenUpperBoundConstraint.setCoefficient(teacherTimeEven, -1.0); // TeacherTime <= suma(xEven)
                oddUpperBoundConstraint.setCoefficient(teacherTimeOdd, -1.0);
            }
        }
    }

    private void handleWeeks(ConstraintBuilder evenConstraint, ConstraintBuilder oddConstraint,
                             Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap, String frequency,
                             int evenCoefficient, int oddCoefficient,
                             int group, int room, int time, int subject, int teacher){
        String varNameEven = "xEven_" + group + "_" + room + "_" + time + "_" + subject + "_" + teacher;
        String varNameOdd = "xOdd_" + group + "_" + room + "_" + time + "_" + subject + "_" + teacher;
        if (frequency.equals(WEEKLY) || frequency.equals(EVEN_WEEKS)) {
            MPVariable evenVar = xEvenMap.get(varNameEven);
            evenConstraint.setCoefficient(evenVar, evenCoefficient);
        }
        if (frequency.equals(WEEKLY) || frequency.equals(ODD_WEEKS)) {
            MPVariable oddVar = xOddMap.get(varNameOdd);
            oddConstraint.setCoefficient(oddVar, oddCoefficient);
        }
    }

    private void iterateAllGroupsSubjectsTeachers(ConstraintBuilder evenConstraint, ConstraintBuilder oddConstraint,
                                                  Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap,
                                                  int coefficient, int time, int room, String frequency){
        for (int p = 0; p < numSubjects; ++p) {
            String subjectId = subjects.get(p).getId();
            List<String> allGroups = groupListService.getGroupsFromGroupMapping(subjects.get(p).getGroupMappings());
            if(!classroomToSubjectTypes.get(rooms.get(room)).contains(subjectId))
                continue;
            for (int n = 0; n < numTeachers; ++n) {
                if(!subjectTypeToTeachers.get(subjectId).contains(teachers.get(n)))
                    continue;
                for (int g = 0; g < numGroups; ++g) {
                    if(!subjects.get(p).getTeachers().contains(teachers.get(n)) || !allGroups.contains(groups.get(g)))
                        continue;
                    handleWeeks(evenConstraint, oddConstraint, xEvenMap, xOddMap, frequency, coefficient, coefficient,
                            g, room, time, p, n);
                }
            }
        }
    }

    private void iterateAllRoomsTeachersSubjects(ConstraintBuilder evenConstraint, ConstraintBuilder oddConstraint,
                                                 Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap,
                                                 int coefficient, int group, int time, String frequency){

        for (int subject = 0; subject < numSubjects; ++subject){
            PlannerClassType plannerSubject = subjects.get(subject);
            String subjectId = plannerSubject.getId();
            List<String> allGroups = groupListService.getGroupsFromGroupMapping(plannerSubject.getGroupMappings());

            if(!allGroups.contains(groups.get(group)))
                continue;

            for (int teacher = 0; teacher < numTeachers; ++teacher){

                if(!plannerSubject.getTeachers().contains(teachers.get(teacher)) ||
                        !subjectTypeToTeachers.get(subjectId).contains(teachers.get(teacher)))
                    continue;

                for (int room = 0; room < numRooms; ++room){

                    if(!classroomToSubjectTypes.get(rooms.get(room)).contains(subjectId))
                        continue;

                    handleWeeks(evenConstraint, oddConstraint, xEvenMap, xOddMap, frequency, coefficient, coefficient,
                            group, room, time, subject, teacher);
                }
            }
        }
    }

    private void iterateAllRoomsSubjectsGroups(ConstraintBuilder evenConstraint, ConstraintBuilder oddConstraint,
                                               Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap,
                                               int coefficient, int teacher, int time, String frequency) {
        for (int subject = 0; subject < numSubjects; ++subject){
            PlannerClassType plannerSubject = subjects.get(subject);
            String subjectId = plannerSubject.getId();
            List<String> allGroups = groupListService.getGroupsFromGroupMapping(plannerSubject.getGroupMappings());
            if(!subjectTypeToTeachers.get(subjectId).contains(teachers.get(teacher)))
                continue;
            for (int group = 0; group < numGroups; ++group){
                if(!plannerSubject.getTeachers().contains(teachers.get(teacher)) || !allGroups.contains(groups.get(group)))
                    continue;
                for (int room = 0; room < numRooms; room++){
                    if(!classroomToSubjectTypes.get(rooms.get(room)).contains(subjectId))
                        continue;
                    handleWeeks(evenConstraint, oddConstraint, xEvenMap, xOddMap, frequency, coefficient, coefficient,
                            group, room, time, subject, teacher);
                }
            }
        }
    }

    public void cleanup() {
        this.groups = null;
        this.teachers = null;
        this.rooms = null;
        this.timeSlots = null;
        this.subjects = null;
        this.teacherLoadList = null;
        this.evenTimeSlots = null;
        this.oddTimeSlots = null;
        this.subjectTypeToTeachers = null;
        this.groupToSubjectTypes = null;
        this.classroomToSubjectTypes = null;
    }
}
