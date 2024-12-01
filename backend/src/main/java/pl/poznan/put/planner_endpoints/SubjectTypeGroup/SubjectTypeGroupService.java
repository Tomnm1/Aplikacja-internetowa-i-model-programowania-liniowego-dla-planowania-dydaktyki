package pl.poznan.put.planner_endpoints.SubjectTypeGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

@Service
public class SubjectTypeGroupService {
    @Autowired
    private SubjectTypeGroupRepository subjectTypeGroupRepository;

    public SubjectTypeGroup createSubjectTypeGroup(SubjectTypeGroup subjectTypeGroup){
        return subjectTypeGroupRepository.save(subjectTypeGroup);
    }
    @Transactional
    public List<SubjectTypeGroup> findBySubjectType(SubjectType subjectType){
        return subjectTypeGroupRepository.findBySubjectType(subjectType);
    }

    public boolean groupHasSubjectType(int groupId, int subjectTypeId){
        return subjectTypeGroupRepository.existsByGroupIdAndSubjectTypeSubjectTypeId(groupId, subjectTypeId);
    }

    public List<SubjectTypeGroup> getAllSubjectTypeGroup(){
        return subjectTypeGroupRepository.findAll();
    }
}
