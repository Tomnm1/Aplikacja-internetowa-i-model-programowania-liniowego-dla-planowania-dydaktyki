package pl.poznan.put.planner_endpoints.Classroom;


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
 * Controller for Classroom resource
 */
@RestController
@RequestMapping("/classrooms")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomservice;

    @Operation(summary = "Return all Classrooms")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Classroom.class))
            )
    })
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public List<Classroom> getAllRoom() {
        return classroomservice.getAllClassrooms();
    }

    @Operation(summary = "Return Classroom by buliding and number")
    @GetMapping(path = "/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Classroom.class)
            )
    })
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public Optional<Classroom> getRoomByID(@PathVariable("number") Integer id) {
        return classroomservice.getRoomByID(id);
    }

    @Operation(summary = "Create Classroom from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Classroom.class))
            )
    })
    public Classroom createRoom(@RequestBody Classroom classroom){
        return classroomservice.createRoom(classroom);
    }

    @Operation(summary = "Update specified Classroom from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Classroom.class))
            )
    })
    public Classroom updateRoomByID(@PathVariable("id") Integer id, @RequestBody Classroom classroomParams){
        return classroomservice.updateRoomByID(id, classroomParams);
    }

    @Operation(summary = "Delete all classrooms")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllClassrooms() {
        classroomservice.deleteAllClassrooms();
    }

    @Operation(summary = "Delete specified room")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteRoom(@PathVariable("id") Integer id) {
        classroomservice.deleteRoomByID(id);
    }
}
