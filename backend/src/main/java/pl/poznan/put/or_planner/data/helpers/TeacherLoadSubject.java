package pl.poznan.put.or_planner.data.helpers;

import java.util.List;

public class TeacherLoadSubject {
    private String name;
    private List<String> groups;
    private String maxGroups;

    public TeacherLoadSubject(String name, List<String> groups, String maxGroups){
        this.groups = groups;
        this.maxGroups = maxGroups;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaxGroups() {
        return maxGroups;
    }

    public void setMaxGroups(String maxGroups) {
        this.maxGroups = maxGroups;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
