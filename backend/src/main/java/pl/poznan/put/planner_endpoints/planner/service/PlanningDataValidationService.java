package pl.poznan.put.planner_endpoints.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.or_planner.data.PlannerData;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.data.helpers.TeacherLoadSubject;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectTypeService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlanningDataValidationService {
    private final SubjectTypeService subjectTypeService;

    @Autowired
    PlanningDataValidationService(
            SubjectTypeService subjectTypeService
    ){
        this.subjectTypeService = subjectTypeService;
    }

    public void executeValidations(PlannerData plannerData){
        this.validateTeachersLoad(plannerData);
    }

    private void validateTeachersLoad(PlannerData plannerData){
        List<TeacherLoad> teacherLoadList = plannerData.getTeachersLoad();
        Map<String, Set<String>> subjectTypeToGroup = plannerData.getSubjectTypeToGroup();

        Map<String, Integer> subjectTypeToMaxGroup = teacherLoadList.stream()
                .flatMap(teacherLoad -> teacherLoad.getTeacherLoadSubjectList().stream())
                .collect(Collectors.toMap(
                        TeacherLoadSubject::getName,
                        subject -> Integer.parseInt(subject.getMaxGroups()),
                        Integer::sum
                ));

        for(String subjectTypeId: subjectTypeToMaxGroup.keySet()){
            Integer teachersLoadMaxGroups = subjectTypeToMaxGroup.get(subjectTypeId);
            Integer subjectTypeGroupNumber = subjectTypeToGroup.get(subjectTypeId).size();
            SubjectType subjectType = subjectTypeService.getsubjectTypeByID(Integer.valueOf(subjectTypeId)).get();
            if(subjectType.type == ClassTypeOwn.wykład && teachersLoadMaxGroups > 0)
                continue;
            else if(subjectType.type == ClassTypeOwn.ćwiczenia && teachersLoadMaxGroups < (subjectTypeGroupNumber / 2)){
                System.out.println(generateMsgForTeacherLoad(subjectType, teachersLoadMaxGroups, subjectTypeGroupNumber/2));
            }
            else if((subjectType.type == ClassTypeOwn.laboratoria || subjectType.type == ClassTypeOwn.projekt) &&
                    teachersLoadMaxGroups < subjectTypeGroupNumber) {
                System.out.println(generateMsgForTeacherLoad(subjectType, teachersLoadMaxGroups, subjectTypeGroupNumber));
            }
        }
    }

    private String generateMsgForTeacherLoad(SubjectType subjectType, int teachersLoadMaxGroups, int subjectTypeGroupNumber){
        return subjectType.subjectTypeId + " Dla: "+ subjectType.subject.semester.specialisation.name + " "
                + subjectType.subject.name + " " + subjectType.type
                + " nauczycielom przydzielono: " + teachersLoadMaxGroups + " a do przydziału było: "
                + subjectTypeGroupNumber;
    }
}
