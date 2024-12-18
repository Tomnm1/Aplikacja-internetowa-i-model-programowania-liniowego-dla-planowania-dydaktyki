package pl.poznan.put.data_import.insert_to_db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.poznan.put.planner_endpoints.Building.BuildingService;
import pl.poznan.put.planner_endpoints.Classroom.ClassroomService;
import pl.poznan.put.data_import.model.buildings.Building;
import pl.poznan.put.data_import.model.buildings.Classroom;
import pl.poznan.put.data_import.model.buildings.ClassroomAttribute;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.poznan.put.constans.Constants.HelperMethods.assignIfNotNull;

@Component
public class BuildingHandler {
    private final BuildingService buildingService;
    private final ClassroomService classroomService;
    private pl.poznan.put.planner_endpoints.Building.Building building;
    private pl.poznan.put.planner_endpoints.Classroom.Classroom classroom;

    @Autowired
    public BuildingHandler(
            BuildingService buildingService,
            ClassroomService classroomService
    ){
        this.buildingService = buildingService;
        this.classroomService = classroomService;
    }

    public void insertBuildings(List<Building> buildings){
        for(Building building: buildings){
            pl.poznan.put.planner_endpoints.Building.Building dbBuilding =
                    new pl.poznan.put.planner_endpoints.Building.Building();
            dbBuilding.code = building.getCode();
            this.building = assignIfNotNull(buildingService.createBuildingIfNotExists(dbBuilding), this.building);

            List<Classroom> classrooms = building.getClassrooms();
            insertClassrooms(classrooms, this.building);
        }
    }

    private void insertClassrooms(List<Classroom> classrooms,
                                  pl.poznan.put.planner_endpoints.Building.Building building){
        for(Classroom classroom: classrooms){
            pl.poznan.put.planner_endpoints.Classroom.Classroom dbClassroom =
                    new pl.poznan.put.planner_endpoints.Classroom.Classroom();
            dbClassroom.building = building;
            dbClassroom.code = classroom.getNumber();
            dbClassroom.capacity = classroom.getCapacity();
            dbClassroom.equipment = getClassroomAttributesAsMap(classroom);
            this.classroom = assignIfNotNull(classroomService.createClassroomIfNotExists(dbClassroom), this.classroom);
        }
    }

    public Map<String, Boolean> getClassroomAttributesAsMap(Classroom classroom) {
        List<ClassroomAttribute> attributes = classroom.getClassroomAttributes();

        if (attributes == null) {
            return Collections.emptyMap();
        }

        return classroom.getClassroomAttributes().stream().collect(Collectors.toMap(
                ClassroomAttribute::getAttribute, attribute -> true
        ));
    }
}
