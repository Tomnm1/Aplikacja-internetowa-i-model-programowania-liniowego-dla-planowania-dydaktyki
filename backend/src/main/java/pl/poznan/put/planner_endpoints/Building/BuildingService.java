package pl.poznan.put.planner_endpoints.Building;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.poznan.put.planner_endpoints.Room.RoomCompositeKey;

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
