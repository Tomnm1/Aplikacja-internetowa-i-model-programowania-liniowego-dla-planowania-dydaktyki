package pl.poznan.put.or_planner.objective;

import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.or_planner.data.helpers.GroupListService;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.TeacherPreferences;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ObjectiveManager {
    private MPObjective objective;
    private List<String> teachers;
    private List<String> timeSlots;
    private List<String> groups;
    private List<String> rooms;
    private List<PlannerClassType> subjects;
    private Map<String, Set<String>> teachersToSubjectTypes;
    private Map<String, Set<String>> subjectTypeToTeachers;
    private Map<String, Set<String>> groupToSubjectTypes;
    private Map<String, Set<String>> classroomToSubjectTypes;
    private final GroupListService groupListService;

    @Autowired
    ObjectiveManager(
            GroupListService groupListService
    ){
        this.groupListService = groupListService;
    }

    public void initialize(List<String> teachers, List<String> timeSlots, MPObjective objective, List<String> groups,
                           List<String> rooms, List<PlannerClassType> subjects, Map<String, Set<String>> teachersToSubjectTypes,
                           Map<String, Set<String>> subjectTypeToTeachers, Map<String, Set<String>> groupToSubjectTypes,
                           Map<String, Set<String>> classroomToSubjectTypes){
        this.objective = objective;
        this.teachers = teachers;
        this.timeSlots = timeSlots;
        this.groups = groups;
        this.rooms = rooms;
        this.subjects = subjects;
        this.teachersToSubjectTypes = teachersToSubjectTypes;
        this.subjectTypeToTeachers = subjectTypeToTeachers;
        this.groupToSubjectTypes = groupToSubjectTypes;
        this.classroomToSubjectTypes = classroomToSubjectTypes;
    }

    public void manageTeacherPreferences(Map<String, MPVariable> xEvenMap, Map<String, MPVariable> xOddMap,
                                         List<TeacherPreferences> teacherPreferencesList){
        for (TeacherPreferences teacherPreferences: teacherPreferencesList) {
            Integer teacherId = teachers.indexOf(teacherPreferences.getTeacherId());
            Map<String, Integer> preferences = teacherPreferences.getPreferences();

            for (String slotId: preferences.keySet()) {
                Integer solverSlotId = timeSlots.indexOf(slotId);
                int preferenceValue = preferences.get(slotId);

                setCoefficient(xEvenMap, teacherId, solverSlotId, preferenceValue);

                setCoefficient(xOddMap, teacherId, solverSlotId, preferenceValue);
            }
        }
    }

    private void setCoefficient(Map<String, MPVariable> xEvenMap, Integer teacherId, Integer slotId, int preferenceValue) {
        String regex = "(xOdd|xEven)_\\d+_\\d+_" + slotId + "_\\d+_" + teacherId;
        Pattern pattern = Pattern.compile(regex);
        for (Map.Entry<String, MPVariable> varEntry : xEvenMap.entrySet()) {
            String varName = varEntry.getKey();
            MPVariable var = varEntry.getValue();

            Matcher matcher = pattern.matcher(varName);
            if(matcher.matches()) {
                String[] variableParams = varName.split("_");
                String groupId = groups.get(Integer.parseInt(variableParams[1]));
                String roomId = rooms.get(Integer.parseInt(variableParams[2]));
                PlannerClassType subjectType = subjects.get(Integer.parseInt(variableParams[4]));

                Set<String> mainGroupsSet = subjectType.getGroupMappings().keySet();

                if (teachersToSubjectTypes.get(teachers.get(teacherId)).contains(subjectType.getId())
                        && classroomToSubjectTypes.get(roomId).contains(subjectType.getId())
                        && groupToSubjectTypes.get(groupId).contains(subjectType.getId())
                        && mainGroupsSet.contains(groupId)) {
                    objective.setCoefficient(var, preferenceValue);
                }
            }
        }
    }

    public void cleanup(){
        this.teachers = null;
        this.timeSlots = null;
        this.objective.clear();
        this.objective = null;
    }
}
