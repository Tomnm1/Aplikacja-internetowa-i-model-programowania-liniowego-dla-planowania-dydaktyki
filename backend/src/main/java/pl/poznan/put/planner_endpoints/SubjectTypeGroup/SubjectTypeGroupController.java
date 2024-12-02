package pl.poznan.put.planner_endpoints.SubjectTypeGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("subjectTypeGroups")
public class SubjectTypeGroupController {
    @Autowired
    private SubjectTypeGroupService subjectTypeGroupService;


}
