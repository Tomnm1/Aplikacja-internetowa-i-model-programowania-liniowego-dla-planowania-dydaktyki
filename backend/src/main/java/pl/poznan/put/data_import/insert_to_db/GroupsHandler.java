package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Group.GroupService;
import pl.poznan.put.planner_endpoints.Semester.Semester;

import java.util.Map;

@Component
public class GroupsHandler {
    private final GroupService groupService;

    @Autowired
    public GroupsHandler(
            GroupService groupService
    ){
        this.groupService = groupService;
    }

//    TODO: Dodać podstawowe podgrupy, szczegółowych na podstawie tego excela wywnioskować sie nie da
    public void processAndInsertGroups(Map<String, Integer> groups, Semester semester){
        for(String g: groups.keySet()){
            for(int i = 1; i <= groups.get(g); i++) {
                Group group = new Group();
                group.code = g + i;
                group.semester = semester;
                groupService.createGroup(group);
            }
        }
    }
}
