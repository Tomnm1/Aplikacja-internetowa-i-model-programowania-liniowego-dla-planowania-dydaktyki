package pl.poznan.put.planner_endpoints.Group;

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
 * Controller for Group resource
 */
@RestController // TODO use http response builders to create success and fail responses
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Operation(summary = "Return all Groups")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Group.class))
            )
    })
    public List<Group> getAllGroup() {
        return groupService.getAllGroup();
    }

    @Operation(summary = "Return Group by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Group.class)
            )
    })
    public Optional<Group> getGroupByID(@PathVariable("id") Integer id) {
        return groupService.getGroupByID(id);
    }

    @Operation(summary = "Create Group from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Group.class)
            )
    })
    // TODO add check to fail if entity already exists
    public Group createGroup(@RequestBody Group group){
        return groupService.createGroup(group);
    }

    @Operation(summary = "Update specified Group from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Group.class)
            )
    })
    public Group updateGroupByID(@PathVariable("id") Integer id, @RequestBody Group groupParams){
        return groupService.updateGroupByID(id, groupParams);
    }

    @Operation(summary = "Delete all Groups")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllGroups() {
        groupService.deleteAllGroups();
    }

    @Operation(summary = "Delete specified Group")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteGroup(@PathVariable("id") Integer id) {
        groupService.deleteGroupByID(id);
    }
}
