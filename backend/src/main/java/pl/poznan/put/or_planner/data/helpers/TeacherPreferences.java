package pl.poznan.put.or_planner.data.helpers;

import java.util.Map;

public class TeacherPreferences {
    private String teacherId;
    private Map<String, Integer> preferences; // key - slotId, value 1/-1

    public String getTeacherId() {
        return teacherId;
    }

    public void setPreferences(Map<String, Integer> preferences) {
        this.preferences = preferences;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Map<String, Integer> getPreferences() {
        return preferences;
    }
}
