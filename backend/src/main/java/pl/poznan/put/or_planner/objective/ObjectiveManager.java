package pl.poznan.put.or_planner.objective;

import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import org.springframework.stereotype.Service;
import pl.poznan.put.or_planner.data.helpers.TeacherPreferences;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ObjectiveManager {
    private MPObjective objective;
    private List<String> teachers;
    private List<String> timeSlots;

    public void initialize(List<String> teachers, List<String> timeSlots, MPObjective objective){
        this.objective = objective;
        this.teachers = teachers;
        this.timeSlots = timeSlots;
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
            if (matcher.matches()) {
                objective.setCoefficient(var, preferenceValue);
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
