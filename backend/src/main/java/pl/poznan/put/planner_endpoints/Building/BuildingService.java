package pl.poznan.put.planner_endpoints.Building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for buildings
 */
@Service
public class BuildingService { // TODO refactor update methods
    @Autowired
    private BuildingRepository buildingRepository;

    /**
     * return all buildings
     * @return list of all building objects
     */
    public List<Building> getAllBuildings() {return buildingRepository.findAll(Sort.by(Sort.Direction.ASC, "building"));}

    /**
     * Finds room by ID
     * @param building name of building
     * @return Optional - empty or with Building
     */
    public Optional<Building> getBuildingByID(String building) {return buildingRepository.findById(building);}

    /**
     * Creates a building
     * @param building building object to be inserted into DB
     * @return saved building
     */
    public Building createBuilding(Building building) {return buildingRepository.save(building);}

    /**
     * Updates a building by given ID and params
     * @param id id of building to update
     * @param buildingParams params to update
     * @return Optional - null or with updated building
     */
    public Building updateBuildingByID(String id, Building buildingParams){
        Optional<Building> building = buildingRepository.findById(id);
        if (building.isPresent()){
            Building oldBuilding = building.get();
            return buildingRepository.save(oldBuilding);
        } else {
            return null;
        }
    }

    /**
     * Deletes building by ID
     * @param building name of building
     */
    public void deleteBuildingByID(String building){
        buildingRepository.deleteById(building);
    }

    /**
     * Deletes all buildings
     */
    public void deleteAllBuildings(){
        buildingRepository.deleteAll();
    }
}
