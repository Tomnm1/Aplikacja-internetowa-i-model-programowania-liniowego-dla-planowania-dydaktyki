package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Subject.Language;
import pl.poznan.put.planner_endpoints.Subject.Subject;
import pl.poznan.put.planner_endpoints.Subject.SubjectService;

@Component
public class SubjectHandler {
    private final SubjectService subjectService;

    @Autowired
    public SubjectHandler(
            SubjectService subjectService
    ){
        this.subjectService = subjectService;
    }

    public Subject insertSubject(String name, boolean exam, boolean mandatory, boolean planned, Language language, Semester semester){
        Subject subject = new Subject();
        subject.name = name;
        subject.exam = exam;
        subject.mandatory = mandatory;
        subject.planned = planned;
        subject.language = language;
        subject.semester = semester;
        return subjectService.createSubjectIfNotExists(subject);
    }
}
