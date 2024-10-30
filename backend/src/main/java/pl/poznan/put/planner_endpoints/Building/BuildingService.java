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
public class BuildingService {
    @Autowired
    private BuildingRepository buildingRepository;

    /**
     * return all buildings
     * @return list of all building objects
     */
    public List<Building> getAllBuildings() {return buildingRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));}

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
}
