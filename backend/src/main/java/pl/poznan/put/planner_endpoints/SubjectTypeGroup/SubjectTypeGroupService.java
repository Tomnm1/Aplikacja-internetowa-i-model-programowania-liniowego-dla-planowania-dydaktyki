package pl.poznan.put.planner_endpoints.SubjectTypeGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectTypeGroupService {
    @Autowired
    private SubjectTypeGroupRepository subjectTypeGroupRepository;

    public SubjectTypeGroup createSubjectTypeGroup(SubjectTypeGroup subjectTypeGroup){
        return subjectTypeGroupRepository.save(subjectTypeGroup);
    }
}
