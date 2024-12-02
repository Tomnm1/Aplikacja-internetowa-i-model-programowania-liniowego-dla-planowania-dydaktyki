package pl.poznan.put.planner_endpoints.SubjectTypeTeacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("subjectTypeTeacher")
public class SubjectTypeTeacherController {
    @Autowired
    private SubjectTypeTeacherService subjectTypeTeacherService;
}
