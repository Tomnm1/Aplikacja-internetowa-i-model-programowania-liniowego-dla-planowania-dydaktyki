package pl.poznan.put.or_planner.data.helpers;

import java.util.List;

public class PlannerSubject {
    private String name;
    private List<String> groups;
    private List<PlannerClassType> typesOfClasses;

    public PlannerSubject(String name, List<String> groups, List<PlannerClassType> typesOfClasses) {
        this.name = name;
        this.groups = groups;
        this.typesOfClasses = typesOfClasses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGroups(){
        return groups;
    }

    public void setGroups(List<String> groups){
        this.groups = groups;
    }

    public List<PlannerClassType> getTypesOfClasses() {
        return typesOfClasses;
    }

    public void setTypesOfClasses(List<PlannerClassType> typesOfClasses) {
        this.typesOfClasses = typesOfClasses;
    }
}