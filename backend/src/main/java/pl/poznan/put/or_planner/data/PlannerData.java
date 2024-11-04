package pl.poznan.put.or_planner.data;

import pl.poznan.put.or_planner.data.helpers.PlannerSubject;

import java.util.List;

public class PlannerData {
    private List<String> groups;
    private List<String> teachers;
    private List<String> rooms;
    private List<String> timeSlots;
    private List<PlannerSubject> subjects;

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
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

    public List<PlannerSubject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<PlannerSubject> subjects) {
        this.subjects = subjects;
    }
}
