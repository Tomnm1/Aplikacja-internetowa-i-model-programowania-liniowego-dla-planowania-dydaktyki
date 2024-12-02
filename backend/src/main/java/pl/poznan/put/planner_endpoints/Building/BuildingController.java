package pl.poznan.put.planner_endpoints.Building;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for Building resource
 */
@RestController
@RequestMapping("/buildings")
public class BuildingController {
    @Autowired
    private BuildingService BuildingService;

    @Operation(summary = "Return all Buildings")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Building.class))
            )
    })
    public List<Building> getAllBuildings() {
        return BuildingService.getAllBuildings();
    }

    @Operation(summary = "Return all Buildings with theirs Classrooms")
    @GetMapping("/classrooms")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Building.class))
            )
    })
    public List<BuildingDTO> getAllBuildingsDTO() {
        return BuildingService.getAllBuildingsDTO();
    }

    @Operation(summary = "Return Buildings by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Building.class)
            )
    })
    public Optional<Building> getBuildingByID(@PathVariable("id") Integer buildingId) {
        return BuildingService.getBuildingByID(buildingId);
    }

    @Operation(summary = "Create Buildings from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Building.class)
            )
    })
    public Building createBuilding(@RequestBody Building Building){
        return BuildingService.createBuilding(Building);
    }

    @Operation(summary = "Update specified Buildings from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Building.class)
            )
    })
    public Building updateBuildingByID(@PathVariable("id") Integer buildingId, @RequestBody Building BuildingParams){
        return BuildingService.updateBuildingByID(buildingId, BuildingParams);
    }

    @Operation(summary = "Delete all Buildings")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllBuilding() {
        BuildingService.deleteAllBuildings();
    }

    @Operation(summary = "Delete specified Building")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteBuilding(@PathVariable("id") Integer buildingId) {
        BuildingService.deleteBuildingByID(buildingId);
    }
}

