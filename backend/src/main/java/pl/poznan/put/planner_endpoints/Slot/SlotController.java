package pl.poznan.put.planner_endpoints.Slot;

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
 * Controller for Slot resource
 */
@RestController
@RequestMapping("/Slots")
public class SlotController {
    @Autowired
    private pl.poznan.put.planner_endpoints.Slot.SlotService SlotService;

    @Operation(summary = "Return all Slots")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Slot.class))
            )
    })
    public List<Slot> getAllSlots() {
        return SlotService.getAllSlots();
    }

    @Operation(summary = "Return Slots by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Slot.class)
            )
    })
    public Optional<Slot> getSlotByID(@PathVariable("id") Integer SlotId) {
        return SlotService.getSlotByID(SlotId);
    }

    @Operation(summary = "Create Slots from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Slot.class)
            )
    })
    public Slot createSlot(@RequestBody Slot Slot){
        return SlotService.createSlot(Slot);
    }

    @Operation(summary = "Update specified Slots from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Slot.class)
            )
    })
    public Slot updateSlotByID(@PathVariable("id") Integer SlotId, @RequestBody Slot SlotParams){
        return SlotService.updateSlotByID(SlotId, SlotParams);
    }

    @Operation(summary = "Delete all Slots")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllSlot() {
        SlotService.deleteAllSlots();
    }

    @Operation(summary = "Delete specified Slot")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteSlot(@PathVariable("id") Integer SlotId) {
        SlotService.deleteSlotByID(SlotId);
    }
}

