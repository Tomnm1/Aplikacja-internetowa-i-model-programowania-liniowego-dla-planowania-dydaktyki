package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.data_import.model.subjects.SubjectWithGroupData;
import pl.poznan.put.data_import.model.subjects.TeacherWithInnerId;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Group.GroupService;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectTypeService;
import pl.poznan.put.planner_endpoints.SubjectTypeGroup.SubjectTypeGroup;
import pl.poznan.put.planner_endpoints.SubjectTypeGroup.SubjectTypeGroupService;
import pl.poznan.put.planner_endpoints.SubjectTypeTeacher.SubjectTypeTeacher;
import pl.poznan.put.planner_endpoints.SubjectTypeTeacher.SubjectTypeTeacherService;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;
import pl.poznan.put.planner_endpoints.Teacher.TeacherService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pl.poznan.put.constans.Constans.ExcelToDb.ColumnNames.LAB_LETTER;


@Component
public class GroupsHandler {
    private final GroupService groupService;
    private final SubjectTypeService subjectTypeService;
    private final SubjectTypeGroupService subjectTypeGroupService;
    private final TeacherService teacherService;
    private final SubjectTypeTeacherService subjectTypeTeacherService;

    @Autowired
    public GroupsHandler(
            GroupService groupService,
            SubjectTypeService subjectTypeService,
            SubjectTypeGroupService subjectTypeGroupService,
            TeacherService teacherService,
            SubjectTypeTeacherService subjectTypeTeacherService
    ){
        this.groupService = groupService;
        this.subjectTypeService = subjectTypeService;
        this.subjectTypeGroupService = subjectTypeGroupService;
        this.teacherService = teacherService;
        this.subjectTypeTeacherService = subjectTypeTeacherService;
    }

    public void processAndInsertGroups(Map<String, Integer> groups, Semester semester,
                                       Map<String, List<SubjectWithGroupData>> groupTypesData){
        List<Group> groupList = new ArrayList<>();
        Map.Entry<String, Integer> maxNumGroups = groups.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        for(int i = 1; i <= Objects.requireNonNull(maxNumGroups).getValue(); i++) {
            Group group = new Group();
            group.code = LAB_LETTER + i;
            group.semester = semester;
            Group returnedGroup = groupService.createGroupIfNotExists(group);
            groupList.add(returnedGroup);
        }
        assignSubjectToGroup(groups, semester, groupTypesData, groupList);
    }

    private void assignSubjectToGroup(Map<String, Integer> groups, Semester semester,
                                     Map<String, List<SubjectWithGroupData>> groupTypesData, List<Group> groupList){
        Map<String, List<List<SubjectWithGroupData>>> electiveFilter = new HashMap<>();
        for(String subject: groupTypesData.keySet()){
            List<SubjectWithGroupData> subjectWithGroupDataList = groupTypesData.get(subject);
            String electivePrefix = obtainPrefix(subject);
            if(electivePrefix != null){
                electiveFilter.computeIfAbsent(electivePrefix, key -> new ArrayList<>())
                        .add(subjectWithGroupDataList);
            } else {
                for(SubjectWithGroupData subjectWithGroupData: subjectWithGroupDataList){
                    SubjectType subjectType = subjectWithGroupData.subjectType();
                    for(Group group: groupList) {
                        addGroupToSubject(subjectType, group);
                    }
                }
            }
            assignTeachersToSubjectType(subjectWithGroupDataList);
        }
        if(!electiveFilter.isEmpty()){
            handleElectiveSubjects(electiveFilter, groupList);
        }
    }

    private void assignTeachersToSubjectType(List<SubjectWithGroupData> subjectWithGroupDataList){
        for(SubjectWithGroupData subjectWithGroupData: subjectWithGroupDataList){
            List<TeacherWithInnerId> assignedTeachers = subjectWithGroupData.assignedTeachers();
            for (TeacherWithInnerId teacherWithInnerId: assignedTeachers){
                Teacher teacher = new Teacher();
                if (Objects.equals(teacherWithInnerId.getInnerId(), "123456789")){
                    teacher = teacherService.findRandomTeacher();
                } else {
                    teacher = teacherService.findByInnerId(Integer.parseInt(teacherWithInnerId.getInnerId()));
                }
                SubjectTypeTeacher subjectTypeTeacher = new SubjectTypeTeacher();
                subjectTypeTeacher.subjectType = subjectWithGroupData.subjectType();
                subjectTypeTeacher.teacher = teacher;
                subjectTypeTeacher.numHours = teacherWithInnerId.getNumHours();
                subjectTypeTeacherService.createSubjectTypeTeacher(subjectTypeTeacher);
            }
        }
    }

    private void handleElectiveSubjects(Map<String, List<List<SubjectWithGroupData>>> electiveFilter,
                                        List<Group> groupList){
        for(String elective: electiveFilter.keySet()){
            List<Integer> groupsRatio = new ArrayList<>();
            for(List<SubjectWithGroupData> subjects: electiveFilter.get(elective)){
                groupsRatio.add(subjects.getLast().numGroups());
            }
//            Integer groupRatio = (int) Math.ceil((double) groupsRatio.stream().reduce(0, Integer::sum) / groupList.size());
            Integer groupRatio = (groupList.size() / groupsRatio.stream().reduce(0, Integer::sum));

            int firstGroupIndex = 0;
            int ratioIndex = 0;
            for(List<SubjectWithGroupData> subjects: electiveFilter.get(elective)){
                int lastGroupIndex = firstGroupIndex + groupRatio * groupsRatio.get(ratioIndex);
                for(SubjectWithGroupData subjectWithGroupData: subjects){
                    for(int i = firstGroupIndex; i < lastGroupIndex; i++){
                        addGroupToSubject(subjectWithGroupData.subjectType(), groupList.get(i));
                    }
                }
                firstGroupIndex = lastGroupIndex;
                ratioIndex++;
            }
        }
    }

    private void addGroupToSubject(SubjectType subjectType, Group group){
        SubjectTypeGroup subjectTypeGroup = new SubjectTypeGroup();
        subjectTypeGroup.subjectType = subjectType;
        subjectTypeGroup.group = group;
        subjectTypeGroupService.createSubjectTypeGroup(subjectTypeGroup);
    }

    private String obtainPrefix(String subject) {
        String[] patterns = {
                "PO\\d+",
                "Przedmiot obieralny \\d+"
        };

        for (String pattern : patterns) {
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(subject);

            if (matcher.find()) {
                return matcher.group();
            }
        }

        return null;
    }
}
