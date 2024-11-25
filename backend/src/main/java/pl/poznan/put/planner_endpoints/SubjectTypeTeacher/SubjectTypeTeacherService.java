package pl.poznan.put.planner_endpoints.SubjectTypeTeacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectTypeTeacherService {
    @Autowired
    private SubjectTypeTeacherRepository subjectTypeTeacherRepository;

    public SubjectTypeTeacher createSubjectTypeTeacher(SubjectTypeTeacher subjectTypeTeacher){
        return subjectTypeTeacherRepository.save(subjectTypeTeacher);
    }
}
