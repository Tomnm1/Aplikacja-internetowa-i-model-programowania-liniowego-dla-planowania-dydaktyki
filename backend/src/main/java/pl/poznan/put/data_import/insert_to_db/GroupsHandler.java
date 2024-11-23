package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.data_import.model.subjects.SubjectWithGroupData;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Group.GroupService;
import pl.poznan.put.planner_endpoints.Semester.Semester;
import pl.poznan.put.planner_endpoints.Subgroup.Subgroup;
import pl.poznan.put.planner_endpoints.Subgroup.SubgroupService;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.math3.util.ArithmeticUtils.lcm;
import static pl.poznan.put.constans.Constans.ExcelToDb.ColumnNames.LECTURE_LETTER;

@Component
public class GroupsHandler {
    private final GroupService groupService;
    private final SubgroupService subgroupService;

    @Autowired
    public GroupsHandler(
            GroupService groupService,
            SubgroupService subgroupService
    ){
        this.groupService = groupService;
        this.subgroupService = subgroupService;
    }

    public void processAndInsertGroups(Map<String, Integer> groups, Semester semester,
                                       Map<String, List<SubjectWithGroupData>> groupTypesData){
        setMainLectureGroup(groups, semester, groupTypesData);
//        for(String g: groups.keySet()){
//            for(int i = 1; i <= groups.get(g); i++) {
//                Group group = new Group();
//                group.code = g + i;
//                group.semester = semester;
//                groupService.createGroupIfNotExists(group);
//            }
//        }
    }

    private void setMainLectureGroup(Map<String, Integer> groups, Semester semester,
                                     Map<String, List<SubjectWithGroupData>> groupTypesData){
        Map<String, Integer> electiveSubjectsLectureGroupsCounter = new HashMap<>();

        Group rootLectureGroup = new Group();
        rootLectureGroup.semester = semester;
        rootLectureGroup.subjectTypesList = new ArrayList<>();
        rootLectureGroup.code = "MainRoot";

        //create exercise groups
//        for(int i = 0; i < groups.get(EXERCISE_LETTER); i++){
//
//        }

        //TODO: Do zmiany tu wszystko praktycznie
        for(String subject: groupTypesData.keySet()){
            if(subject.matches("PO\\d{1,2}.*") || subject.matches("PO\\d{1,2} -.*") || subject.matches("Przedmiot obieralny \\d{1,2}:.*")) {
                for(SubjectWithGroupData subjectType: groupTypesData.get(subject)){
                    if(subjectType.type() == ClassTypeOwn.wykład){
                        String prefix = obtainPrefix(subject) + LECTURE_LETTER;
                        electiveSubjectsLectureGroupsCounter.put(prefix,
                                electiveSubjectsLectureGroupsCounter.getOrDefault(prefix, 0) + 1);
                    }
                }
                continue;
            }
            for(SubjectWithGroupData subjectType: groupTypesData.get(subject)){
                if(subjectType.type() == ClassTypeOwn.wykład){
                    rootLectureGroup.group_type = ClassTypeOwn.wykład;
                    rootLectureGroup.subjectTypesList.add(subjectType.subjectType());
                }
            }
        }
        groupService.createGroup(rootLectureGroup);

    }

    private String obtainPrefix(String subject) {
        String[] patterns = {
                "PO\\d+:",
                "PO\\d+ -",
                "Przedmiot obieralny \\d+:"
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
