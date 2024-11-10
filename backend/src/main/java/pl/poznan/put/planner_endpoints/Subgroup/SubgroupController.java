package pl.poznan.put.planner_endpoints.Subgroup;

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
 * Controller for Subgroup resource
 */
@RestController
@RequestMapping("/subgroups")
public class SubgroupController {
    @Autowired
    private SubgroupService subgroupService;

    @Operation(summary = "Return all Subgroups")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Subgroup.class))
            )
    })
    public List<Subgroup> getAllSubgroup() {
        return subgroupService.getAllSubgroup();
    }

    @Operation(summary = "Return Subgroup by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Subgroup.class)
            )
    })
    public Optional<Subgroup> getSubgroupByID(@PathVariable("id") Integer id) {
        return subgroupService.getSubgroupByID(id);
    }

    @Operation(summary = "Create Subgroup from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Subgroup.class)
            )
    })
    public Subgroup createSubgroup(@RequestBody Subgroup subgroup){
        return subgroupService.createSubgroup(subgroup);
    }

    @Operation(summary = "Update specified Subgroup from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Subgroup.class)
            )
    })
    public Subgroup updateSubgroupByID(@PathVariable("id") Integer id, @RequestBody Subgroup subgroupParams){
        return subgroupService.updateSubgroupByID(id, subgroupParams);
    }

    @Operation(summary = "Delete all Subgroups")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllSubgroups() {
        subgroupService.deleteAllSubgroups();
    }

    @Operation(summary = "Delete specified Subgroup")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteSubgroup(@PathVariable("id") Integer id) {
        subgroupService.deleteSubgroupByID(id);
    }
}
