package pl.poznan.put.planner_endpoints.SubjectTypeGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

@Service
public class SubjectTypeGroupService {
    @Autowired
    private SubjectTypeGroupRepository subjectTypeGroupRepository;

    public SubjectTypeGroup createSubjectTypeGroup(SubjectTypeGroup subjectTypeGroup){
        return subjectTypeGroupRepository.save(subjectTypeGroup);
    }

    public List<SubjectTypeGroup> findBySubjectType(SubjectType subjectType){
        return subjectTypeGroupRepository.findBySubjectType(subjectType);
    }
}
