package pl.poznan.put.planner_endpoints.SlotsDay;

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
 * Controller for SlotsDay resource
 */
@RestController
@RequestMapping("/SlotsDays")
public class SlotsDayController {
    @Autowired
    private pl.poznan.put.planner_endpoints.SlotsDay.SlotsDayService SlotsDayService;

    @Operation(summary = "Return all SlotsDays")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = SlotsDay.class))
            )
    })
    public List<SlotsDay> getAllSlotsDays() {
        return SlotsDayService.getAllSlotsDays();
    }

    @Operation(summary = "Return SlotsDays by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SlotsDay.class)
            )
    })
    public Optional<SlotsDay> getSlotsDayByID(@PathVariable("id") Integer slotsDayId) {
        return SlotsDayService.getSlotsDayByID(slotsDayId);
    }

    @Operation(summary = "Create SlotsDays from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SlotsDay.class)
            )
    })
    public SlotsDay createSlotsDay(@RequestBody SlotsDay slotsDay){
        return SlotsDayService.createSlotsDay(slotsDay);
    }

    @Operation(summary = "Update specified SlotsDays from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SlotsDay.class)
            )
    })
    public SlotsDay updateSlotsDayByID(@PathVariable("id") Integer slotsDayId, @RequestBody SlotsDay slotsDayParams){
        return SlotsDayService.updateSlotsDayByID(slotsDayId, slotsDayParams);
    }

    @Operation(summary = "Delete all SlotsDays")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllSlotsDay() {
        SlotsDayService.deleteAllSlotsDays();
    }

    @Operation(summary = "Delete specified SlotsDay")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteSlotsDay(@PathVariable("id") Integer slotsDayId) {
        SlotsDayService.deleteSlotsDayByID(slotsDayId);
    }
}

