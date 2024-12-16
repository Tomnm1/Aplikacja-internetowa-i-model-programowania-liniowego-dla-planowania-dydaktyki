package pl.poznan.put.or_planner;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.or_planner.constraints.ConstraintsManager;
import pl.poznan.put.or_planner.data.helpers.GroupListService;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.data.helpers.TeacherPreferences;
import pl.poznan.put.or_planner.insert.PlannedSlot;
import pl.poznan.put.or_planner.objective.ObjectiveManager;
import pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes.ClassroomSubjectTypeService;
import pl.poznan.put.planner_endpoints.SubjectTypeGroup.SubjectTypeGroupService;
import pl.poznan.put.planner_endpoints.SubjectTypeTeacher.SubjectTypeTeacherService;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class Planner {
    final
    SubjectTypeTeacherService subjectTypeTeacherService;
    final
    SubjectTypeGroupService subjectTypeGroupService;
    final
    ConstraintsManager constraintsManager;
    final
    ObjectiveManager objectiveManager;
    final
    GroupListService groupListService;
    final
    ClassroomSubjectTypeService classroomSubjectTypeService;
    private Map<String, Set<String>> subjectTypeToTeachers;
    private Map<String, Set<String>> groupToSubjectTypes;
    private Map<String, Set<String>> classroomToSubjectTypes;
    private Map<String, Set<String>> teachersToSubjectTypes;

    private List<String> groups;
    private List<String> teachers;
    private List<String> rooms;
    private List<String> timeSlots;
    private List<PlannerClassType> subjects;
    private List<TeacherLoad> teacherLoadList;
    private List<TeacherPreferences> teacherPreferences;

    private int numGroups;
    private int numRooms;
    private int numSubjects;
    private int numTimeSlots;
    private int numTeachers;

    private List<String> evenTimeSlots;
    private List<String> oddTimeSlots;

    private static final Logger logger = Logger.getLogger(Planner.class.getName());

    @Autowired
    public Planner(
            SubjectTypeTeacherService subjectTypeTeacherService,
            SubjectTypeGroupService subjectTypeGroupService,
            ConstraintsManager constraintsManager,
            ClassroomSubjectTypeService classroomSubjectTypeService,
            ObjectiveManager objectiveManager,
            GroupListService groupListService) {
        this.subjectTypeTeacherService = subjectTypeTeacherService;
        this.subjectTypeGroupService = subjectTypeGroupService;
        this.constraintsManager = constraintsManager;
        this.classroomSubjectTypeService = classroomSubjectTypeService;
        this.objectiveManager = objectiveManager;
        this.groupListService = groupListService;
    }

    public void initialize(List<String> groups, List<String> teachers, List<String> rooms, List<String> timeSlots,
                           List<PlannerClassType> subjects, List<TeacherLoad> teacherLoadList, List<TeacherPreferences> teacherPreferences,
                           Map<String, Set<String>> subjectTypeToTeachers, Map<String, Set<String>> groupToSubjectTypes,
                           Map<String, Set<String>> classroomToSubjectTypes, Map<String, Set<String>> teachersToSubjectTypes){
        Loader.loadNativeLibraries();
        this.groups = groups;
        this.teachers = teachers;
        this.rooms = rooms;
        this.timeSlots = timeSlots;
        this.subjects = subjects;
        this.teacherLoadList = teacherLoadList;
        this.teacherPreferences = teacherPreferences;

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

        for (String slot : timeSlots) {
            evenTimeSlots.add(slot + "_even");
            oddTimeSlots.add(slot + "_odd");
        }
    }

    public List<PlannedSlot> optimizeSchedule() throws FileNotFoundException {
        logger.log(Level.INFO, "Schedule optimization started");
        MPSolver solver = MPSolver.createSolver("SCIP");
//        MPObjective objective = solver.objective();

        // Variables - dana grupa g, w danej sali s, o danym czasie t, ma przedmiot p z nauczycielem n
        logger.log(Level.INFO, "tab na zmienne");

        Map<String, MPVariable> xEvenMap = new HashMap<>();
        Map<String, MPVariable> xOddMap = new HashMap<>();

        logger.log(Level.INFO, "zmienne");
        for (int p = 0; p < numSubjects; ++p) {
            String subjectId = subjects.get(p).getId();
            List<String> allGroups = groupListService.getGroupsFromGroupMapping(subjects.get(p).getGroupMappings());
            for (int n = 0; n < numTeachers; ++n) {
                if(!subjectTypeToTeachers.get(subjectId).contains(teachers.get(n)))
                    continue;
                for (int g = 0; g < numGroups; ++g) {
                    if(!allGroups.contains(groups.get(g)))
                        continue;
                    for (int s = 0; s < numRooms; ++s) {
                        if(!classroomToSubjectTypes.get(rooms.get(s)).contains(subjectId))
                            continue;
                        for (int t = 0; t < evenTimeSlots.size(); ++t) {
                            String varNameEven = "xEven_" + g + "_" + s + "_" + t + "_" + p + "_" + n;
                            String varNameOdd = "xOdd_" + g + "_" + s + "_" + t + "_" + p + "_" + n;

                            xEvenMap.put(varNameEven, solver.makeBoolVar(varNameEven));
                            xOddMap.put(varNameOdd, solver.makeBoolVar(varNameOdd));
                        }
                    }
                }
            }
        }

        logger.log(Level.INFO, "zmienne przypisane");

        constraintsManager.initialize(solver, groups, teachers, rooms, timeSlots, subjects, teacherLoadList,
                subjectTypeToTeachers, groupToSubjectTypes, classroomToSubjectTypes, teachersToSubjectTypes);

        logger.log(Level.INFO, " ograniczenie 1");
        constraintsManager.addRoomOccupationConstraint(xEvenMap, xOddMap);

        logger.log(Level.INFO, " ograniczenie 2");
        constraintsManager.assignSubjectsToGroupsAndBlockGroupsConstraint(xEvenMap, xOddMap);

        logger.log(Level.INFO, " ograniczenie 3");
        constraintsManager.oneTeacherOneClassConstraint(xEvenMap, xOddMap);

        logger.log(Level.INFO, " ograniczenie 4");
        constraintsManager.oneGroupOneClassConstraint(xEvenMap, xOddMap);

        logger.log(Level.INFO, " ograniczenie 5");
        constraintsManager.teachersLoadConstraint(xEvenMap, xOddMap);

//        logger.log(Level.INFO, "Przypisanie funkcji celu");
//        objectiveManager.initialize(teachers, timeSlots, objective, groups, rooms, subjects, teachersToSubjectTypes,
//                subjectTypeToTeachers, groupToSubjectTypes, classroomToSubjectTypes);
//        objectiveManager.manageTeacherPreferences(xEvenMap, xOddMap, teacherPreferences);
//        logger.log(Level.INFO, "Funkcja celu przypisana");

        logger.log(Level.INFO, " solver start");
        solver.enableOutput();

//        try (PrintWriter out = new PrintWriter("filename.txt")) {
//            out.println(solver.exportModelAsLpFormat());
//        }

//        objective.setMaximization();
        MPSolverParameters solverParameters = new MPSolverParameters();

//            solver.setTimeLimit(120*1000);
        MPSolver.ResultStatus status = solver.solve(solverParameters);
        logger.log(Level.INFO, " solver stop");
        if (status == MPSolver.ResultStatus.OPTIMAL || status == MPSolver.ResultStatus.FEASIBLE) {
            logger.log(Level.INFO, "Found a " + status + " solution!");
//            logger.log(Level.INFO, "Objective value = " + objective.value());
        } else {
            logger.log(Level.INFO, "No feasible solution found");
        }
        logger.log(Level.INFO, "Solution analysing started");
        List<PlannedSlot> scheduleTable = new ArrayList<>();

        xEvenMap.forEach((xEvenName, xEven) -> {
            if(xEven.solutionValue() == 1)
                prepareAndAddToResultTable(xEven, scheduleTable, true);
        });

        xOddMap.forEach((xOddName, xOdd) -> {
            if(xOdd.solutionValue() == 1)
                prepareAndAddToResultTable(xOdd, scheduleTable, false);
        });

        logger.log(Level.INFO, "Solution analyzing finished");
        xOddMap.clear();
        xEvenMap.clear();
        constraintsManager.cleanup();
//        objectiveManager.cleanup();
        solver.delete();
        return scheduleTable;
    }

    private void prepareAndAddToResultTable(MPVariable variable,  List<PlannedSlot> scheduleTable, boolean isEven){
        String variableName = variable.name();
        String[] variableParams = variableName.split("_");
        String groupId = groups.get(Integer.parseInt(variableParams[1]));
        String roomId = rooms.get(Integer.parseInt(variableParams[2]));
        String slotId = timeSlots.get(Integer.parseInt(variableParams[3]));
        PlannerClassType subjectType = subjects.get(Integer.parseInt(variableParams[4]));
        String teacherId = teachers.get(Integer.parseInt(variableParams[5]));

        Map<String, List<String>> groupMappings = subjectType.getGroupMappings();
        List<String> groupsToAssign = groupMappings.get(groupId);
        if(groupsToAssign == null) {
            System.out.println(subjectType.getId() + " " + teacherId);
        }

        PlannedSlot plannedSlot = new PlannedSlot(slotId, groupId, teacherId, roomId, subjectType.getId(), isEven);
        scheduleTable.add(plannedSlot);

        for(String group: groupsToAssign){
            PlannedSlot evenGroupSlot = new PlannedSlot(slotId, group, teacherId, roomId, subjectType.getId(), isEven);
            scheduleTable.add(evenGroupSlot);
        }
    }

    public void cleanup() {
        this.groups = null;
        this.teachers = null;
        this.rooms = null;
        this.timeSlots = null;
        this.subjects = null;
        this.teacherLoadList = null;
        this.teacherPreferences = null;
        this.evenTimeSlots = null;
        this.oddTimeSlots = null;
        this.subjectTypeToTeachers = null;
        this.groupToSubjectTypes = null;
        this.classroomToSubjectTypes = null;
    }
}

// fiat voluntas tua