package pl.poznan.put.planner_endpoints.Subject;


import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Semester.SemesterRepository;
import pl.poznan.put.planner_endpoints.Semester.SemesterService;

public class SubjectDTO {

    public Integer SubjectId;

    public String name;

    public Language language;

    public Boolean exam;

    public Boolean mandatory;

    public Boolean planned;

    public Semester semester;

    public Boolean checkClassrooms;

    public Boolean checkGroups;

    public Boolean checkTeachers;

    public SubjectDTO(Integer subjectId, Semester semester, String name, Boolean exam, Boolean mandatory,
                      Boolean planned, Language language, Boolean checkClassrooms, Boolean checkGroups, Boolean checkTeachers) {
        this.SubjectId = subjectId;
        this.semester = semester;
        this.name = name;
        this.exam = exam;
        this.mandatory = mandatory;
        this.planned = planned;
        this.language = language;
        this.checkClassrooms = checkClassrooms;
        this.checkGroups = checkGroups;
        this.checkTeachers = checkTeachers;
    }
}
