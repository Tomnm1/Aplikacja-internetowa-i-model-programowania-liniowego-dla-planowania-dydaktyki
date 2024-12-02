package pl.poznan.put.planner_endpoints.Building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Classroom.ClassroomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for buildings
 */
@Service
public class BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ClassroomRepository classroomRepository;

    /**
     * return all buildings
     * @return list of all building objects
     */
    public List<Building> getAllBuildings() {return buildingRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));}


    public List<BuildingDTO> getAllBuildingsDTO() {
        List<BuildingDTO> buildingDTOS = new ArrayList<>();
        List<Building> buildings =  buildingRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));
        for( Building building : buildings ) {
            BuildingDTO dto = new BuildingDTO();
            dto.code = building.code;
            dto.classroomList =  classroomRepository.findClassroomsByBuilding(building);
            buildingDTOS.add(dto);
        }
        return buildingDTOS;}
    /**
     * Finds room by ID
     * @param buildingId ID of building
     * @return Optional - empty or with Building
     */
    public Optional<Building> getBuildingByID(Integer buildingId) {return buildingRepository.findById(buildingId);}

    /**
     * Creates a building
     * @param building building object to be inserted into DB
     * @return saved building
     */
    public Building createBuilding(Building building) {return buildingRepository.save(building);}

    /**
     * Updates a building by given ID and params
     * @param buildingId id of building to update
     * @param buildingParams params to update
     * @return Optional - null or with updated building
     */
    public Building updateBuildingByID(Integer buildingId, Building buildingParams){
        Optional<Building> building = buildingRepository.findById(buildingId);
        if (building.isPresent()){
            Building oldBuilding = building.get();
            oldBuilding.code = buildingParams.code;
            return buildingRepository.save(oldBuilding);
        } else {
            return null;
        }
    }

    /**
     * Deletes building by ID
     * @param buildingId ID of building
     */
    public void deleteBuildingByID(Integer buildingId){
        buildingRepository.deleteById(buildingId);
    }

    /**
     * Deletes all buildings
     */
    public void deleteAllBuildings(){
        buildingRepository.deleteAll();
    }

    public Building createBuildingIfNotExists(Building building){
        Building existingBuilding = buildingRepository.findByCode(building.code);
        if(existingBuilding != null){
            return existingBuilding;
        } else {
            createBuilding(building);
            return building;
        }
    }

}
