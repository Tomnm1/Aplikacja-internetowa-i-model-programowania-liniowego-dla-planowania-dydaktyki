package pl.poznan.put.or_planner.data;

import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.data.helpers.TeacherPreferences;
import pl.poznan.put.planner_endpoints.Teacher.Degree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlannerData {
    private List<String> groups;
    private List<String> teachers;
    private List<String> rooms;
    private List<String> timeSlots;
    private List<PlannerClassType> subjects;
    private List<TeacherLoad> teachersLoad;
    private List<TeacherPreferences> teacherPreferences;
    private Map<String, Set<String>> subjectTypeToTeachers;
    private Map<String, Set<String>> groupToSubjectTypes;
    private Map<String, Set<String>> classroomToSubjectTypes;
    private Map<String, Set<String>> teachersToSubjectTypes;
    private Map<String, Set<String>> subjectTypeToGroup;
    private Set<String> teachersWithPreferences;
    private Map<String, Degree> teacherToDegree = new HashMap<>();

    public PlannerData() {
    }

    public void setTeachersWithPreferences(Set<String> teachersWithPreferences) {
        this.teachersWithPreferences = teachersWithPreferences;
    }

    public Set<String> getTeachersWithPreferences() {
        return teachersWithPreferences;
    }

    public void setTeacherToDegree(Map<String, Degree> teacherToDegree) {
        this.teacherToDegree = teacherToDegree;
    }

    public Map<String, Degree> getTeacherToDegree() {
        return teacherToDegree;
    }

    public List<TeacherPreferences> getTeacherPreferences() {
        return teacherPreferences;
    }

    public void setTeacherPreferences(List<TeacherPreferences> teacherPreferences) {
        this.teacherPreferences = teacherPreferences;
    }

    public void setSubjectTypeToGroup(Map<String, Set<String>> subjectTypeToGroup){
        this.subjectTypeToGroup = subjectTypeToGroup;
    }

    public Map<String, Set<String>> getSubjectTypeToGroup() {
        return subjectTypeToGroup;
    }

    public Map<String, Set<String>> getTeachersToSubjectTypes() {
        return teachersToSubjectTypes;
    }

    public void setTeachersToSubjectTypes(Map<String, Set<String>> teachersToSubjectTypes) {
        this.teachersToSubjectTypes = teachersToSubjectTypes;
    }

    public List<TeacherLoad> getTeachersLoad() {
        return teachersLoad;
    }

    public void setTeachersLoad(List<TeacherLoad> teachersLoad) {
        this.teachersLoad = teachersLoad;
    }

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

    public List<PlannerClassType> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<PlannerClassType> subjects) {
        this.subjects = subjects;
    }

    public void setClassroomToSubjectTypes(Map<String, Set<String>> classroomToSubjectTypes) {
        this.classroomToSubjectTypes = classroomToSubjectTypes;
    }

    public Map<String, Set<String>> getClassroomToSubjectTypes() {
        return classroomToSubjectTypes;
    }

    public Map<String, Set<String>> getSubjectTypeToTeachers() {
        return subjectTypeToTeachers;
    }

    public Map<String, Set<String>> getGroupToSubjectTypes() {
        return groupToSubjectTypes;
    }

    public void setSubjectTypeToTeachers(Map<String, Set<String>> subjectTypeToTeachers) {
        this.subjectTypeToTeachers = subjectTypeToTeachers;
    }

    public void setGroupToSubjectTypes(Map<String, Set<String>> groupToSubjectTypes) {
        this.groupToSubjectTypes = groupToSubjectTypes;
    }
}
