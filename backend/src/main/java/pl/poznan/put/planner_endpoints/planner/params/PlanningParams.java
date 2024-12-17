package pl.poznan.put.planner_endpoints.planner.params;

public class PlanningParams {
    private final String fieldOfStudyType;
    private final String semesterType;
    private final String planName;

    public PlanningParams(String fieldOfStudyType, String semesterType, String planName) {
        this.fieldOfStudyType = fieldOfStudyType;
        this.semesterType = semesterType;
        this.planName = planName;
    }

    public String getFieldOfStudyType() {
        return fieldOfStudyType;
    }

    public String getSemesterType() {
        return semesterType;
    }

    public String getPlanName() {
        return planName;
    }
}
