package pl.poznan.put.planner_endpoints.planner.params;

public class PlanningParams {
    private final String fieldOfStudyType;
    private final String semesterType;

    public PlanningParams(String fieldOfStudyType, String semesterType) {
        this.fieldOfStudyType = fieldOfStudyType;
        this.semesterType = semesterType;
    }

    public String getFieldOfStudyType() {
        return fieldOfStudyType;
    }

    public String getSemesterType() {
        return semesterType;
    }
}
