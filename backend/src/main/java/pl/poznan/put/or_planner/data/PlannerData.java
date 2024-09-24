package pl.poznan.put.or_planner.data;

import java.util.List;
import java.util.Map;

public class PlannerData {
    private List<String> groups;
    private List<String> subjects;
    private List<String> rooms;
    private List<String> teachers;
    private List<String> timeSlots;
    private Map<String, List<String>> roomToSubjects;
    private Map<String, List<String>> subjectsToTeachers;
    private Map<String, List<String>> groupsToSubjects;

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getRooms() {
        return rooms;
    }

    public void setRooms(List<String> rooms) {
        this.rooms = rooms;
    }

    public List<String> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<String> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public Map<String, List<String>> getRoomToSubjects() {
        return roomToSubjects;
    }

    public void setRoomToSubjects(Map<String, List<String>> roomToSubjects) {
        this.roomToSubjects = roomToSubjects;
    }

    public Map<String, List<String>> getSubjectsToTeachers() {
        return subjectsToTeachers;
    }

    public void setSubjectsToTeachers(Map<String, List<String>> subjectsToTeachers) {
        this.subjectsToTeachers = subjectsToTeachers;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public void setGroupsToSubjects(Map<String, List<String>> groupsToSubjects){ this.groupsToSubjects = groupsToSubjects; }
    public Map<String, List<String>> getGroupsToSubjects() { return groupsToSubjects; }
}
