package pl.poznan.put.planner_endpoints.planner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.or_planner.data.PlannerData;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.data.helpers.TeacherLoadSubject;
import pl.poznan.put.planner_endpoints.Classroom.ClassroomService;
import pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes.ClassroomSubjectTypeService;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Group.GroupService;
import pl.poznan.put.planner_endpoints.SlotsDay.SlotsDayService;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectTypeService;
import pl.poznan.put.planner_endpoints.SubjectTypeGroup.SubjectTypeGroup;
import pl.poznan.put.planner_endpoints.SubjectTypeGroup.SubjectTypeGroupService;
import pl.poznan.put.planner_endpoints.SubjectTypeTeacher.SubjectTypeTeacher;
import pl.poznan.put.planner_endpoints.SubjectTypeTeacher.SubjectTypeTeacherService;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;

import java.util.*;
import java.util.stream.Collectors;

import static pl.poznan.put.constans.Constans.Weeks.*;

@Service
public class PlanningDataAssemblingService {
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final SlotsDayService slotsDayService;
    private final SubjectTypeService subjectTypeService;
    private final ClassroomSubjectTypeService classroomSubjectTypeService;
    private final SubjectTypeGroupService subjectTypeGroupService;
    private final SubjectTypeTeacherService subjectTypeTeacherService;
    private boolean week;

    @Autowired
    public PlanningDataAssemblingService(
            GroupService groupService,
            TeacherService teacherService,
            ClassroomService classroomService,
            SlotsDayService slotsDayService,
            SubjectTypeService subjectTypeService,
            ClassroomSubjectTypeService classroomSubjectTypeService,
            SubjectTypeGroupService subjectTypeGroupService,
            SubjectTypeTeacherService subjectTypeTeacherService
    ) {
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.slotsDayService = slotsDayService;
        this.subjectTypeService = subjectTypeService;
        this.classroomSubjectTypeService = classroomSubjectTypeService;
        this.subjectTypeGroupService = subjectTypeGroupService;
        this.subjectTypeTeacherService = subjectTypeTeacherService;
    }

    public PlannerData startAssembling(String exclude){
        this.week = false;
        PlannerData plannerData = new PlannerData();
        plannerData.setGroups(getAllGroups(exclude));
        plannerData.setTeachers(getAllAssignedTeachers());
        plannerData.setRooms(getAllAssignedClassrooms());
        plannerData.setTimeSlots(getAllTimeSlots());
        plannerData.setSubjects(getAllSubjects(exclude));
        plannerData.setTeachersLoad(getTeachersLoad(exclude));
        return plannerData;
    }

    private List<TeacherLoad> getTeachersLoad(String exclude){
        List<Teacher> teachers = teacherService.getAllteachers();
        List<TeacherLoad> result = new ArrayList<>();
        for(Teacher teacher: teachers){
            List<TeacherLoadSubject> teacherLoadSubjectList = new ArrayList<>();

            List<SubjectTypeTeacher> subjectTypeTeachers = subjectTypeTeacherService.findByTeacher(teacher);
            for(SubjectTypeTeacher subjectTypeTeacher: subjectTypeTeachers){
                List<String> groups = subjectTypeGroupService.findBySubjectType(subjectTypeTeacher.subjectType)
                        .stream()
                        .map(subjectTypeGroup -> subjectTypeGroup.group.id.toString())
                        .toList();
                String maxGroups = String.valueOf((subjectTypeTeacher.numHours / subjectTypeTeacher.subjectType.numOfHours));

                teacherLoadSubjectList.add(
                    new TeacherLoadSubject(
                        subjectTypeTeacher.subjectType.subjectTypeId.toString(),
                        groups,
                        maxGroups)
                    );

            }
            result.add(new TeacherLoad(teacher.id.toString(), teacherLoadSubjectList));
        }
        return result;
    }

    private List<PlannerClassType> getAllSubjects(String exclude){
        List<PlannerClassType> result = new ArrayList<>();
        List<SubjectType> subjectTypeList = subjectTypeService.getAllsubjectType();
        for(SubjectType subjectType: subjectTypeList){
            if(!subjectType.subject.semester.specialisation.fieldOfStudy.name.endsWith(exclude)){
                PlannerClassType plannerClassType =
                    new PlannerClassType(
                        subjectType.subjectTypeId.toString(),
                        subjectType.type.toString(),
                        this.obtainFrequency(subjectType),
                        this.getAssignedRooms(subjectType),
                        this.getAssignedTeachers(subjectType),
                        this.getGroupMappings(subjectType)
                    );
                result.add(plannerClassType);
            }
        }
        return result;
    }

    private Map<String, List<String>> getGroupMappings(SubjectType subjectType){
        Map<String, List<String>> result = new HashMap<>();
        List<SubjectTypeGroup> subjectTypeGroups = subjectTypeGroupService.findBySubjectType(subjectType);
        if (subjectType.type == ClassTypeOwn.wykład) {
            String key = subjectTypeGroups.get(0).group.id.toString();

            List<String> values = new ArrayList<>();
            for (int i = 1; i < subjectTypeGroups.size(); i++) {
                values.add(subjectTypeGroups.get(i).group.id.toString());
            }

            result.put(key, values);
        } else if ( subjectType.type == ClassTypeOwn.ćwiczenia){
            for (int i = 0; i < subjectTypeGroups.size(); i += 2) {
                String key = subjectTypeGroups.get(i).group.id.toString();

                if (i + 1 < subjectTypeGroups.size()) {
                    String value = subjectTypeGroups.get(i + 1).group.id.toString();
                    result.put(key, Collections.singletonList(value));
                } else {
                    result.put(key, new ArrayList<>());
                }
            }
        } else {
            for (SubjectTypeGroup subjectTypeGroup : subjectTypeGroups) {
                result.put(subjectTypeGroup.group.id.toString(), new ArrayList<>());
            }
        }
        return result;
    }

    private List<String> getAssignedTeachers(SubjectType subjectType){
        return subjectTypeTeacherService.findBySubjectType(subjectType).stream()
                .map(subjectTypeTeacher -> String.valueOf(subjectTypeTeacher.subjectTypeTeacherId))
                .toList();
    }

    private List<String> getAssignedRooms(SubjectType subjectType){
        return classroomSubjectTypeService.findBySubjectType(subjectType).stream()
                .map(classroomSubjectType -> String.valueOf(classroomSubjectType.id))
                .toList();
    }

    private String obtainFrequency(SubjectType subjectType){
        if(subjectType.numOfHours <= 16){
            week = !week;
            if(week) return EVEN_WEEKS;
            else return ODD_WEEKS;
        } else {
            return WEEKLY;
        }
    }

    private List<String> getAllTimeSlots(){
        return slotsDayService.getAllSlotsDays().stream()
                .map(slotsDay -> String.valueOf(slotsDay.SlotsDayId))
                .collect(Collectors.toList());
    }

    private List<String> getAllClassrooms(){
        return classroomService.getAllClassrooms().stream()
                .map(classroom -> String.valueOf(classroom.classroomID))
                .collect(Collectors.toList());
    }

    private List<String> getAllAssignedClassrooms(){
        return classroomSubjectTypeService.getAllAssignedClassrooms().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    private List<String> getAllAssignedTeachers(){
        return subjectTypeTeacherService.findAllAssignedTeachers().stream()
                .map(String::valueOf)
                .toList();
    }

    private List<String> getAllTeachers() {
        return teacherService.getAllteachers().stream()
                .map(teacher -> String.valueOf(teacher.id))
                .collect(Collectors.toList());
    }

    private List<String> getAllGroups(String exclude) {
        List<Group> groups = groupService.getAllGroup();
        List<String> groupIds = new ArrayList<>();
        for (Group group : groups) {
            if (!group.semester.specialisation.fieldOfStudy.name.endsWith(exclude)) {
                groupIds.add(group.id.toString());
            }
        }
        return groupIds;
    }
}
