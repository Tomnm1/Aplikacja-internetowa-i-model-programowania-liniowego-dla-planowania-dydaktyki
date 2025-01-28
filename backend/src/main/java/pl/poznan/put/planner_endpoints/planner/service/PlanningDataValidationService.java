package pl.poznan.put.planner_endpoints.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.or_planner.data.PlannerData;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.data.helpers.TeacherLoadSubject;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectTypeService;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;
import pl.poznan.put.planner_endpoints.planner.error_message.ErrorMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.poznan.put.constans.Constants.ErrorMessage.Critical.*;
import static pl.poznan.put.planner_endpoints.planner.error_message.ErrorType.CRITICAL;
import static pl.poznan.put.planner_endpoints.planner.error_message.ErrorType.IMPORTANT;

@Service
public class PlanningDataValidationService {
    private final SubjectTypeService subjectTypeService;
    private final TeacherService teacherService;
    private boolean hasCriticalError = false;

    @Autowired
    PlanningDataValidationService(
            SubjectTypeService subjectTypeService,
            TeacherService teacherService
    ){
        this.subjectTypeService = subjectTypeService;
        this.teacherService = teacherService;
    }

    public boolean executeValidations(PlannerData plannerData,  List<ErrorMessage> errorMessagesList){
        hasCriticalError = false;
        this.validateBasicLists(plannerData, errorMessagesList);
        this.validateSubjects(plannerData, errorMessagesList);
        this.validateTeachersLoad(plannerData, errorMessagesList);
        return hasCriticalError;
    }

    private void validateBasicLists(PlannerData plannerData, List<ErrorMessage> errorMessagesList){
        if(plannerData.getGroups().isEmpty()){
            errorMessagesList.add(new ErrorMessage(CRITICAL, GROUPS_MISSING));
            hasCriticalError = true;
        }
        if(plannerData.getTeachers().isEmpty()){
            errorMessagesList.add(new ErrorMessage(CRITICAL, TEACHERS_MISSING));
            hasCriticalError = true;
        }
        if(plannerData.getRooms().isEmpty()){
            errorMessagesList.add(new ErrorMessage(CRITICAL, ROOMS_MISSING));
            hasCriticalError = true;
        }
        if(plannerData.getTimeSlots().isEmpty()){
            errorMessagesList.add(new ErrorMessage(CRITICAL, SLOTS_MISSING));
            hasCriticalError = true;
        }
        if(plannerData.getSubjects().isEmpty()){
            errorMessagesList.add(new ErrorMessage(CRITICAL, SUBJECTS_MISSING));
            hasCriticalError = true;
        }
        if(plannerData.getTeachersLoad().isEmpty()){
            errorMessagesList.add(new ErrorMessage(CRITICAL, LOAD_MISSING));
            hasCriticalError = true;
        }
    }

    private void validateSubjects(PlannerData plannerData, List<ErrorMessage> errorMessageList){
        plannerData.getSubjects().forEach(subject -> {
            if(subject.getRooms().isEmpty()) {
                errorMessageList.add(new ErrorMessage(IMPORTANT, this.generateMsgForMissingRoomsForSubject(subject)));
            }
            if(subject.getTeachers().isEmpty()){
                errorMessageList.add(new ErrorMessage(IMPORTANT, this.generateMsgForMissingTeachersForSubject(subject)));
            }
            if(subject.getGroupMappings().isEmpty()){
                errorMessageList.add(new ErrorMessage(IMPORTANT, this.generateMsgForMissingGroupsForSubject(subject)));
            }
        });
    }

    private void validateTeachersLoad(PlannerData plannerData, List<ErrorMessage> errorMessagesList){
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
            if(subjectTypeToGroup.get(subjectTypeId) != null && !subjectTypeToGroup.get(subjectTypeId).isEmpty()) {
                Integer subjectTypeGroupNumber = subjectTypeToGroup.get(subjectTypeId).size();
                SubjectType subjectType = subjectTypeService.getsubjectTypeByID(Integer.valueOf(subjectTypeId)).get();
                if (subjectType.type == ClassTypeOwn.wykład && teachersLoadMaxGroups > 0)
                    continue;
                else if (subjectType.type == ClassTypeOwn.ćwiczenia && teachersLoadMaxGroups < (subjectTypeGroupNumber / 2)) {
                    errorMessagesList.add(new ErrorMessage(IMPORTANT,
                            generateMsgForTeacherLoad(subjectType, teachersLoadMaxGroups, subjectTypeGroupNumber / 2)));
                } else if ((subjectType.type == ClassTypeOwn.laboratoria || subjectType.type == ClassTypeOwn.projekt) &&
                        teachersLoadMaxGroups < subjectTypeGroupNumber) {
                    errorMessagesList.add(new ErrorMessage(IMPORTANT,
                            generateMsgForTeacherLoad(subjectType, teachersLoadMaxGroups, subjectTypeGroupNumber)));
                }
            }
        }
    }

    private String generateMsgForMissingGroupsForSubject(PlannerClassType subject){
        List<String> teachers = subject.getTeachers();
        if(teachers.isEmpty())
            return "Dla przedmiotu: " + subject.getName() + "_" + subject.getType() + " nie przypisano grup.";
        else{
            Teacher teacher = teacherService.getTeacherByID(Integer.parseInt(teachers.get(0))).get();
            return "Przypisanie " + teacher.firstName + " " + teacher.lastName + "dla przedmiotu: " + subject.getName()
                    + "_" + subject.getType() + " jest prawodpodobnie nadmiarowe. Proszę nie stosować nadmiarowych " +
                    "przypisań w całym systemie!";
        }
    }
    private String generateMsgForMissingTeachersForSubject(PlannerClassType subject){
        return "Dla przedmiotu: " + subject.getName() + "_" + subject.getType() + " nie przypisano prowadzących!";
    }

    private String generateMsgForMissingRoomsForSubject(PlannerClassType subject){
        return "Dla przedmiotu: " + subject.getName() + "_" + subject.getType() + " nie przypisano sal!";
    }

    private String generateMsgForTeacherLoad(SubjectType subjectType, int teachersLoadMaxGroups, int subjectTypeGroupNumber){
        return " Dla specjalizacji: "+ subjectType.subject.semester.specialisation.name + "; przedmiotu"
                + subjectType.subject.name + " " + subjectType.type
                + " nauczycielom przydzielono: " + teachersLoadMaxGroups + " grup a do przydziału było: "
                + subjectTypeGroupNumber + " grup. Popraw przydziały.";
    }
}
