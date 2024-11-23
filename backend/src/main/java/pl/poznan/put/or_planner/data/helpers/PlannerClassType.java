package pl.poznan.put.or_planner.data.helpers;

import java.util.List;
import java.util.Map;

public class PlannerClassType {
    private String id;
    private String type;
    private String frequency;
    private List<String> rooms;
    private List<String> teachers;
    private Map<String, List<String>> groupMappings;

    public PlannerClassType
            (String id, String type, String frequency, List<String> rooms, List<String> teachers, List<String> assignedGroups,
             Map<String, List<String>> groupMappings) {
        this.id = id;
        this.type = type;
        this.frequency = frequency;
        this.rooms = rooms;
        this.teachers = teachers;
        this.groupMappings = groupMappings;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public Map<String, List<String>> getGroupMappings() { return groupMappings; }

    public void setGroupMappings(Map<String, List<String>> groupMappings) { this.groupMappings = groupMappings; }

}
