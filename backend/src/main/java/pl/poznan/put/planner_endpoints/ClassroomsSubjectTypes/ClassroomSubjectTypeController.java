package pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classroomSubjectType")
public class ClassroomSubjectTypeController {
    @Autowired
    private ClassroomSubjectTypeService classroomSubjectTypeService;
}
