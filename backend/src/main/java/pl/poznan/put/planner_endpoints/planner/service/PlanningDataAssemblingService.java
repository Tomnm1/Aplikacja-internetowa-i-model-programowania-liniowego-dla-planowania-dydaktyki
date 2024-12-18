package pl.poznan.put.planner_endpoints.planner.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.or_planner.data.PlannerData;
import pl.poznan.put.or_planner.data.helpers.PlannerClassType;
import pl.poznan.put.or_planner.data.helpers.TeacherLoad;
import pl.poznan.put.or_planner.data.helpers.TeacherLoadSubject;
import pl.poznan.put.or_planner.data.helpers.TeacherPreferences;
import pl.poznan.put.planner_endpoints.Classroom.ClassroomService;
import pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes.ClassroomSubjectType;
import pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes.ClassroomSubjectTypeService;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Group.GroupService;
import pl.poznan.put.planner_endpoints.SlotsDay.SlotsDayService;
import pl.poznan.put.planner_endpoints.Subject.Subject;
import pl.poznan.put.planner_endpoints.Subject.SubjectService;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectTypeService;
import pl.poznan.put.planner_endpoints.SubjectTypeGroup.SubjectTypeGroup;
import pl.poznan.put.planner_endpoints.SubjectTypeGroup.SubjectTypeGroupService;
import pl.poznan.put.planner_endpoints.SubjectTypeTeacher.SubjectTypeTeacher;
import pl.poznan.put.planner_endpoints.SubjectTypeTeacher.SubjectTypeTeacherService;
import pl.poznan.put.planner_endpoints.Teacher.Degree;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;
import pl.poznan.put.planner_endpoints.planner.params.PlanningParams;

import java.util.*;
import java.util.stream.Collectors;

import static pl.poznan.put.constans.Constants.Weeks.*;

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
    private final SubjectService subjectService;
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
            SubjectTypeTeacherService subjectTypeTeacherService,
            SubjectService subjectService
    ) {
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.slotsDayService = slotsDayService;
        this.subjectTypeService = subjectTypeService;
        this.classroomSubjectTypeService = classroomSubjectTypeService;
        this.subjectTypeGroupService = subjectTypeGroupService;
        this.subjectTypeTeacherService = subjectTypeTeacherService;
        this.subjectService = subjectService;
    }

    public PlannerData startAssembling(PlanningParams planningParams){
        this.week = false;
        Map<String, String> teacherToSubjectTypeMaxGroups = new HashMap<>();
        Map<String, Degree> teacherToDegree = new HashMap<>();
        String fieldOfStudyType = planningParams.getFieldOfStudyType();
        String semesterType = planningParams.getSemesterType();

        PlannerData plannerData = new PlannerData();
        plannerData.setGroups(getAllAssignedGroups(fieldOfStudyType, semesterType));
        plannerData.setTeachers(getAllAssignedTeachers(teacherToDegree, fieldOfStudyType, semesterType));
        plannerData.setRooms(getAllAssignedClassrooms(fieldOfStudyType, semesterType));
        plannerData.setTimeSlots(getAllTimeSlots());
        plannerData.setTeachersLoad(getTeachersLoad(fieldOfStudyType, semesterType, teacherToSubjectTypeMaxGroups));
        plannerData.setSubjects(getAllSubjects(fieldOfStudyType, semesterType, teacherToSubjectTypeMaxGroups));
        plannerData.setClassroomToSubjectTypes(getClassroomToSubjectTypes());
        plannerData.setGroupToSubjectTypes(getGroupToSubjectTypes());
        plannerData.setSubjectTypeToTeachers(getSubjectTypeToTeachers());

        Map<String, Set<String>> teacherToSubjectTypes = getTeacherToSubjectTypes();
        plannerData.setTeachersToSubjectTypes(teacherToSubjectTypes);
        plannerData.setSubjectTypeToGroup(getSubjectTypeToGroup());

        List<TeacherPreferences> teacherPreferences = getAllTeacherSlotPreferences(teacherToSubjectTypes);
        plannerData.setTeacherPreferences(teacherPreferences);
        plannerData.setTeachersWithPreferences(getTeachersWithPreferences(teacherPreferences));
        plannerData.setTeacherToDegree(teacherToDegree);
        return plannerData;
    }

    private Set<String> getTeachersWithPreferences(List<TeacherPreferences> teacherPreferences){
        return teacherPreferences.stream().map(TeacherPreferences::getTeacherId).collect(Collectors.toSet());
    }

    private Map<String, Set<String>> getSubjectTypeToGroup(){
        List<SubjectTypeGroup> subjectTypeGroupList = subjectTypeGroupService.getAllSubjectTypeGroup();
        Map<String, Set<String>> subjectTypeToGroup = new HashMap<>();
        for(SubjectTypeGroup subjectTypeGroup: subjectTypeGroupList){
            if(subjectTypeGroup.subjectType.subject.planned) {
                String subjectTypeId = subjectTypeGroup.subjectType.subjectTypeId.toString();
                String groupId = subjectTypeGroup.group.id.toString();
                subjectTypeToGroup
                        .computeIfAbsent(subjectTypeId, k -> new HashSet<>())
                        .add(groupId);
            }
        }
        return subjectTypeToGroup;
    }

    private Map<String, Set<String>> getTeacherToSubjectTypes(){
        List<SubjectTypeTeacher> subjectTypeTeacherList = subjectTypeTeacherService.getAllSubjectTypeTeacher();
        Map<String, Set<String>> teachersToSubjectTypes = new HashMap<>();
        for(SubjectTypeTeacher subjectTypeTeacher: subjectTypeTeacherList){
            if(subjectTypeTeacher.subjectType.subject.planned) {
                String subjectTypeId = subjectTypeTeacher.subjectType.subjectTypeId.toString();
                String teacherId = subjectTypeTeacher.teacher.id.toString();
                teachersToSubjectTypes
                        .computeIfAbsent(teacherId, k -> new HashSet<>())
                        .add(subjectTypeId);
            }
        }
        return teachersToSubjectTypes;
    }

    private Map<String, Set<String>> getSubjectTypeToTeachers(){
        List<SubjectTypeTeacher> subjectTypeTeacherList = subjectTypeTeacherService.getAllSubjectTypeTeacher();
        Map<String, Set<String>> subjectTypeToTeachers = new HashMap<>();
        for(SubjectTypeTeacher subjectTypeTeacher: subjectTypeTeacherList){
            if(subjectTypeTeacher.subjectType.subject.planned) {
                String subjectTypeId = subjectTypeTeacher.subjectType.subjectTypeId.toString();
                String teacherId = subjectTypeTeacher.teacher.id.toString();
                subjectTypeToTeachers
                        .computeIfAbsent(subjectTypeId, k -> new HashSet<>())
                        .add(teacherId);
            }
        }
        return subjectTypeToTeachers;
    }

    private Map<String, Set<String>> getGroupToSubjectTypes(){
        List<SubjectTypeGroup> subjectTypeGroupList = subjectTypeGroupService.getAllSubjectTypeGroup();
        Map<String, Set<String>> groupToSubjectTypes = new HashMap<>();
        for(SubjectTypeGroup subjectTypeGroup: subjectTypeGroupList){
            if(subjectTypeGroup.subjectType.subject.planned) {
                String subjectTypeId = subjectTypeGroup.subjectType.subjectTypeId.toString();
                String groupId = subjectTypeGroup.group.id.toString();
                groupToSubjectTypes
                        .computeIfAbsent(groupId, k -> new HashSet<>())
                        .add(subjectTypeId);
            }
        }
        return groupToSubjectTypes;
    }

    private Map<String, Set<String>> getClassroomToSubjectTypes(){
        List<ClassroomSubjectType> classroomSubjectTypeList = classroomSubjectTypeService.getAllClassroomSubjectType();
        Map<String, Set<String>> classroomToSubjectTypes = new HashMap<>();
        for(ClassroomSubjectType classroomSubjectType: classroomSubjectTypeList){
            if(classroomSubjectType.subjectType.subject.planned) {
                String subjectTypeId = classroomSubjectType.subjectType.subjectTypeId.toString();
                String classroomId = classroomSubjectType.classroom.classroomID.toString();
                classroomToSubjectTypes
                        .computeIfAbsent(classroomId, k -> new HashSet<>())
                        .add(subjectTypeId);
            }
        }
        return classroomToSubjectTypes;
    }

    private List<TeacherLoad> getTeachersLoad(String fieldOfStudyType, String semesterType,
                                              Map<String, String> teacherToSubjectTypeMaxGroups){
        List<Teacher> teachers = teacherService.getAllteachers();
        List<TeacherLoad> result = new ArrayList<>();
        for(Teacher teacher: teachers){
            List<TeacherLoadSubject> teacherLoadSubjectList = new ArrayList<>();

            List<SubjectTypeTeacher> subjectTypeTeachers = subjectTypeTeacherService.findByTeacher(teacher);
            for(SubjectTypeTeacher subjectTypeTeacher: subjectTypeTeachers){
                if(!subjectTypeTeacher.subjectType.subject.planned ||
                        !Objects.equals(subjectTypeTeacher.subjectType.subject.semester.typ, semesterType) ||
                        !Objects.equals(subjectTypeTeacher.subjectType.subject.semester.specialisation.fieldOfStudy.typ, fieldOfStudyType))
                    continue;
                List<String> groups = subjectTypeGroupService.findBySubjectType(subjectTypeTeacher.subjectType)
                        .stream()
                        .map(subjectTypeGroup -> subjectTypeGroup.group.id.toString())
                        .toList();
                int teacherNumHoursSubject = subjectTypeTeacher.numHours;
                int subjectTypeHoursForGroup = subjectTypeTeacher.subjectType.numOfHours;
                if(teacherNumHoursSubject < subjectTypeHoursForGroup)
                    teacherNumHoursSubject = subjectTypeHoursForGroup;
                int maxGroups = (int) Math.ceil((double) teacherNumHoursSubject / subjectTypeHoursForGroup);

                if(maxGroups > 0) {
                    String key = subjectTypeTeacher.subjectType.subjectTypeId.toString() + "_" + teacher.id;
                    teacherToSubjectTypeMaxGroups.put(key, String.valueOf(maxGroups));
                    teacherLoadSubjectList.add(
                            new TeacherLoadSubject(
                                    subjectTypeTeacher.subjectType.subjectTypeId.toString(),
                                    groups,
                                    String.valueOf(maxGroups))
                    );
                }
                else{
                    System.out.println(subjectTypeTeacher.subjectType.type + subjectTypeTeacher.subjectType.subject.name);
                }
            }
            if(!teacherLoadSubjectList.isEmpty())
                result.add(new TeacherLoad(teacher.id.toString(), teacherLoadSubjectList));
        }
        return result;
    }

    private List<PlannerClassType> getAllSubjects(String fieldOfStudyType, String semesterType,
                                                  Map<String, String> teacherToSubjectTypeMaxGroups){
        List<PlannerClassType> result = new ArrayList<>();
        List<Subject> subjectList = subjectService.getAllSubject();
        for(Subject subject: subjectList){
            if(subject.planned
            && Objects.equals(subject.semester.specialisation.fieldOfStudy.typ, fieldOfStudyType)
            && Objects.equals(subject.semester.typ, semesterType)){

                List<SubjectType> subjectTypeList = subjectTypeService.getAllSubjectTypeBySubject(subject);
                boolean hasExe = false;
                boolean hasLab = false;
                Map<ClassTypeOwn, PlannerClassType> subjectTypesMap = new HashMap<>();
                for(SubjectType subjectType: subjectTypeList){
                    String frequency = obtainFrequency(subjectType);
                    if(frequency.equals(EVEN_WEEKS) || frequency.equals(ODD_WEEKS)) {
                        if (subjectType.type == ClassTypeOwn.ćwiczenia)
                            hasExe = true;
                        else if (subjectType.type == ClassTypeOwn.laboratoria)
                            hasLab = true;
                    }

                    String subjectTypeId = subjectType.subjectTypeId.toString();
                    String name = subjectType.subject.name;
                    List<String> assignedRooms = this.getAssignedRooms(subjectType);
                    List<String> assignedTeachers = this.getAssignedTeachers(subjectType);
                    Map<String, List<String>> groupMappings = this.getGroupMappings(subjectType);
                    Iterator<Map.Entry<String, List<String>>> groupMappingsIterator = groupMappings.entrySet().iterator();

                    assignedTeachers.forEach(teacher -> {
                        String key = subjectTypeId + "_" + teacher;
                        int maxGroups = Integer.parseInt(teacherToSubjectTypeMaxGroups.get(key));
                        Map<String, List<String>> teacherGroupMappings = new HashMap<>();
                        int groupsAssigned = 0;

                        while (groupsAssigned < maxGroups && groupMappingsIterator.hasNext()) {
                            Map.Entry<String, List<String>> entry = groupMappingsIterator.next();
                            teacherGroupMappings.put(entry.getKey(), entry.getValue());
                            groupsAssigned++;
                        }

                        PlannerClassType plannerClassType =
                                new PlannerClassType(
                                        subjectTypeId,
                                        name,
                                        subjectType.type.toString(),
                                        frequency,
                                        assignedRooms,
                                        List.of(teacher),
                                        teacherGroupMappings
                                );
                        result.addAll(splitIfPossible(plannerClassType));
                    });
//                    subjectTypesMap.put(subjectType.type, plannerClassType);
                }
//                List<PlannerClassType> plannerClassTypeList;
//                if(hasLab && hasExe){
//                    plannerClassTypeList = splitSubjectsForLabAndExe(subjectTypesMap);
//                } else if (hasExe){
//                    plannerClassTypeList = splitSubjectsForExe(subjectTypesMap);
//                } else if (hasLab){
//                    plannerClassTypeList = splitSubjectsForLab(subjectTypesMap);
//                } else {
//                    plannerClassTypeList = new ArrayList<>(subjectTypesMap.values());
//                }
//                result.addAll(plannerClassTypeList);
            }
        }
        return result;
    }

    private List<PlannerClassType> splitIfPossible(PlannerClassType plannerClassType){
        String frequency = plannerClassType.getFrequency();
        if(frequency.equals(WEEKLY))
            return List.of(plannerClassType);
        List<PlannerClassType> result = new ArrayList<>();
        Map<String, List<String>> groupMappings = plannerClassType.getGroupMappings();
        int totalKeys = groupMappings.size();
        int halfSize = totalKeys / 2;
        List<Map<String, List<String>>> newGroupMappings = splitGroupMappings(totalKeys, halfSize, groupMappings);

        return getPlannerClassTypes(result, plannerClassType, frequency, newGroupMappings);
    }

    private List<PlannerClassType> splitSubjectsForExe(Map<ClassTypeOwn, PlannerClassType> subjectTypesMap){
        List<PlannerClassType> result = new ArrayList<>();
        PlannerClassType exeSubjectType = subjectTypesMap.get(ClassTypeOwn.ćwiczenia);
        String frequency = exeSubjectType.getFrequency();
        subjectTypesMap.remove(ClassTypeOwn.ćwiczenia);
        if(!subjectTypesMap.isEmpty()){
            result.addAll(subjectTypesMap.values());
        }

        Map<String, List<String>> exeGroupMappings = exeSubjectType.getGroupMappings();
        int totalKeys = exeGroupMappings.size();
        int halfSize = (totalKeys + 1) / 2;
        List<Map<String, List<String>>> newExeGroupMappings = splitGroupMappings(totalKeys, halfSize, exeGroupMappings);


        return getPlannerClassTypes(result, exeSubjectType, frequency, newExeGroupMappings);
    }

    private List<PlannerClassType> splitSubjectsForLab(Map<ClassTypeOwn, PlannerClassType> subjectTypesMap){
        List<PlannerClassType> result = new ArrayList<>();
        PlannerClassType labSubjectType = subjectTypesMap.get(ClassTypeOwn.laboratoria);
        String frequency = labSubjectType.getFrequency();
        subjectTypesMap.remove(ClassTypeOwn.laboratoria);
        if(!subjectTypesMap.isEmpty()){
            result.addAll(subjectTypesMap.values());
        }

        Map<String, List<String>> labGroupMappings = labSubjectType.getGroupMappings();
        int totalKeys = labGroupMappings.size();
        int halfSize = (totalKeys + 1) / 2;
        List<Map<String, List<String>>> newLabGroupMappings = splitGroupMappings(totalKeys, halfSize, labGroupMappings);

        return getPlannerClassTypes(result, labSubjectType, frequency, newLabGroupMappings);
    }

    private List<PlannerClassType> getPlannerClassTypes(List<PlannerClassType> result, PlannerClassType labSubjectType, String frequency, List<Map<String, List<String>>> newLabGroupMappings) {
        for(Map<String, List<String>> labGroupMapping: newLabGroupMappings){
            PlannerClassType plannerClassType =
                new PlannerClassType(
                    labSubjectType.getId(),
                    labSubjectType.getName(),
                    labSubjectType.getType(),
                    frequency,
                    labSubjectType.getRooms(),
                    labSubjectType.getTeachers(),
                    labGroupMapping
                );
            frequency = getOppositeFrequency(frequency);
            result.add(plannerClassType);
        }
        return result;
    }

    private List<PlannerClassType> splitSubjectsForLabAndExe(Map<ClassTypeOwn, PlannerClassType> subjectTypesMap){
        List<PlannerClassType> result = new ArrayList<>();
        PlannerClassType exeSubjectType = subjectTypesMap.get(ClassTypeOwn.ćwiczenia);
        String frequency = exeSubjectType.getFrequency();
        PlannerClassType labSubjectType = subjectTypesMap.get(ClassTypeOwn.laboratoria);
        subjectTypesMap.remove(ClassTypeOwn.ćwiczenia);
        subjectTypesMap.remove(ClassTypeOwn.laboratoria);
        if(!subjectTypesMap.isEmpty()){
            result.addAll(subjectTypesMap.values());
        }

        Map<String, List<String>> exeGroupMappings = exeSubjectType.getGroupMappings();
        int totalKeys = exeGroupMappings.size();
        int halfSize = (totalKeys + 1) / 2;
        List<Map<String, List<String>>> newExeGroupMappings = splitGroupMappings(totalKeys, halfSize, exeGroupMappings);

        Map<String, List<String>> labGroupMappings = labSubjectType.getGroupMappings();
        totalKeys = labGroupMappings.size();
        List<Map<String, List<String>>> newLabGroupMappings = splitGroupMappings(totalKeys, halfSize * 2, labGroupMappings);

        for(Map<String, List<String>> exeGroupMapping: newExeGroupMappings){
            PlannerClassType plannerClassType =
                new PlannerClassType(
                    exeSubjectType.getId(),
                    exeSubjectType.getName(),
                    exeSubjectType.getType(),
                    frequency,
                    exeSubjectType.getRooms(),
                    exeSubjectType.getTeachers(),
                    exeGroupMapping
                );
            frequency = getOppositeFrequency(frequency);
            result.add(plannerClassType);
        }
        frequency = getOppositeFrequency(frequency);
        return getPlannerClassTypes(result, labSubjectType, frequency, newLabGroupMappings);
    }

    private String getOppositeFrequency(String frequency){
        if(Objects.equals(frequency, ODD_WEEKS)) return EVEN_WEEKS;
        else return ODD_WEEKS;
    }

    private List<Map<String, List<String>>> splitGroupMappings(int totalSize, int splitPoint,
                                                               Map<String, List<String>> groupMappings){
        List<String> keys = new ArrayList<>(groupMappings.keySet());
        List<String> firstPartKeys = keys.subList(0, splitPoint);
        List<String> secondPartKeys = keys.subList(splitPoint, totalSize);

        Map<String, List<String>> firstPartMap = firstPartKeys.stream()
                .collect(Collectors.toMap(key -> key, groupMappings::get));

        Map<String, List<String>> secondPartMap = secondPartKeys.stream()
                .collect(Collectors.toMap(key -> key, groupMappings::get));
        return Arrays.asList(firstPartMap, secondPartMap);
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
                .map(subjectTypeTeacher -> String.valueOf(subjectTypeTeacher.teacher.id))
                .toList();
    }

    private List<String> getAssignedRooms(SubjectType subjectType){
        return classroomSubjectTypeService.findBySubjectType(subjectType).stream()
                .map(classroomSubjectType -> String.valueOf(classroomSubjectType.classroom.classroomID))
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

    private List<String> getAllAssignedClassrooms(String fieldOfStudyType, String semesterType){
        List<String> result = new ArrayList<>();

        List<ClassroomSubjectType> assignedClassrooms = classroomSubjectTypeService.getAllClassroomSubjectType();
        for(ClassroomSubjectType classroomSubjectType: assignedClassrooms){
            if(classroomSubjectType.subjectType.subject.planned &&
            Objects.equals(classroomSubjectType.subjectType.subject.semester.typ, semesterType) &&
            Objects.equals(classroomSubjectType.subjectType.subject.semester.specialisation.fieldOfStudy.typ, fieldOfStudyType) &&
            !result.contains(classroomSubjectType.classroom.classroomID.toString())){
                result.add(classroomSubjectType.classroom.classroomID.toString());
            }
        }
        return result;
    }

    private List<String> getAllAssignedTeachers(Map<String, Degree> teacherToDegree, String fieldOfStudyType, String semesterType){
        List<String> result = new ArrayList<>();

        List<SubjectTypeTeacher> assignedTeachers = subjectTypeTeacherService.getAllSubjectTypeTeacher();

        for(SubjectTypeTeacher subjectTypeTeacher: assignedTeachers){
            if(subjectTypeTeacher.subjectType.subject.planned &&
            Objects.equals(subjectTypeTeacher.subjectType.subject.semester.typ, semesterType) &&
            Objects.equals(subjectTypeTeacher.subjectType.subject.semester.specialisation.fieldOfStudy.typ, fieldOfStudyType) &&
            !result.contains(subjectTypeTeacher.teacher.id.toString())){
                teacherToDegree.put(String.valueOf(subjectTypeTeacher.teacher.id), subjectTypeTeacher.teacher.degree);
                result.add(String.valueOf(subjectTypeTeacher.teacher.id));
            }
        }
        return result;
    }

    private List<String> getAllAssignedGroups(String fieldOfStudyType, String semesterType){
        List<String> result = new ArrayList<>();
        List<SubjectTypeGroup> assignedGroups = subjectTypeGroupService.getAllSubjectTypeGroup();

        for (SubjectTypeGroup subjectTypeGroup : assignedGroups) {
            if (subjectTypeGroup.subjectType.subject.planned &&
                    Objects.equals(subjectTypeGroup.group.semester.specialisation.fieldOfStudy.typ, fieldOfStudyType) &&
                    Objects.equals(subjectTypeGroup.group.semester.typ, semesterType) &&
                    !result.contains(subjectTypeGroup.group.id.toString())) {
                result.add(subjectTypeGroup.group.id.toString());
            }
        }
        return result;
    }

    private List<TeacherPreferences> getAllTeacherSlotPreferences(Map<String, Set<String>> teacherToSubjectTypes) {
        List<Teacher> teachers = teacherService.getAllTeachersWithPreferences();
        return teachers.stream()
                .filter(teacher -> teacherToSubjectTypes.containsKey(teacher.id.toString()))
                .map(teacher -> {
                    TeacherPreferences preferences = new TeacherPreferences();
                    preferences.setTeacherId(String.valueOf(teacher.id));
                    try {
                        String slotsJson = teacher.preferences.get("slots");
                        if (slotsJson != null) {
                            Map<String, Integer> slotsPreferences = new ObjectMapper().readValue(
                                    slotsJson, new TypeReference<>() {
                                    }
                            );

                            preferences.setPreferences(slotsPreferences);
                        } else {
                            preferences.setPreferences(Collections.emptyMap());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Error parsing 'slots' preferences for teacherId: " + teacher.id, e);
                    }
                    return preferences;
                })
                .collect(Collectors.toList());
    }


    private List<String> getAllGroups(String fieldOfStudyType, String semesterType) {
        List<Group> groups = groupService.getAllGroup();
        List<String> groupIds = new ArrayList<>();
        for (Group group : groups) {
            if (Objects.equals(group.semester.specialisation.fieldOfStudy.typ, fieldOfStudyType) &&
                    Objects.equals(group.semester.typ, semesterType)) {
                groupIds.add(group.id.toString());
            }
        }
        return groupIds;
    }
}
