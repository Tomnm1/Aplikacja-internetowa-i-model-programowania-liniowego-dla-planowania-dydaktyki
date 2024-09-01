package pl.poznan.put.planner_endpoints.Room;


import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for Room resource
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Operation(summary = "Return all Rooms")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Room.class))
            )
    })
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public List<Room> getAllRoom() {
        return roomService.getAllRooms();
    }

    @Operation(summary = "Return Room by buliding and number")
    @GetMapping(path = "/{building}/{number}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Room.class)
            )
    })
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public Optional<Room> getRoomByID(@PathVariable("building") String building, @PathVariable("number") Integer number) {
        return roomService.getRoomByID(new RoomCompositeKey(building, number));
    }

    @Operation(summary = "Create Room from provided JSON")
    @PostMapping
    public Room createRoom(@RequestBody Room room){
        // TODO add check to fail if entity already exists
        return roomService.createRoom(room);
    }

    @Operation(summary = "Update specified Room from provided JSON")
    @PutMapping("/{building}/{number}")
    public Room updateRoomByID(@PathVariable("building") String building, @PathVariable("number") Integer number, @RequestBody Room roomParams){
        return roomService.updateRoomByID(new RoomCompositeKey(building, number), roomParams);
    }

    @Operation(summary = "Delete all rooms")
    @DeleteMapping
    public void deleteAllRooms() {
        roomService.deleteAllRooms();
    }

    @Operation(summary = "Delete specified room")
    @DeleteMapping("/{building}/{number}")
    public void deleteRoom(@PathVariable("building") String building, @PathVariable("number") Integer number) {
        roomService.deleteRoomByID(new RoomCompositeKey(building, number));
    }
}
