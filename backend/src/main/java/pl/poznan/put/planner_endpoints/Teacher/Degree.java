package pl.poznan.put.planner_endpoints.Teacher;

public enum Degree {
    BRAK("brak"),
    LIC("lic."),
    INZ("inż."),
    MGR("mgr"),
    MGR_INZ("mgr inż."),
    DR("dr"),
    DR_INZ("dr inż."),
    DR_HAB("dr hab."),
    DR_HAB_INZ("dr hab. inż."),
    DR_PROF_PP("dr, prof. PP"),
    DR_INZ_PROF_PP("dr inż., prof. PP"),
    DR_HAB_PROF_PP("dr hab., prof. PP"),
    DR_HAB_INZ_PROF_PP("dr hab. inż., prof. PP"),
    PROF_DR_HAB("prof. dr hab."),
    PROF_DR_HAB_INZ("prof. dr hab. inż.");

    private final String displayName;

    Degree(String displayName) {
        this.displayName = displayName;
    }

    public String getDegree() {
        return displayName;
    }

}
