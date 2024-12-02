package pl.poznan.put.planner_endpoints.Plan;

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
 * Controller for Plan resource
 */
@RestController
@RequestMapping("/plans")
public class PlanController {
    @Autowired
    private pl.poznan.put.planner_endpoints.Plan.PlanService PlanService;

    @Operation(summary = "Return all Plans")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Plan.class))
            )
    })
    public List<Plan> getAllPlans() {
        return PlanService.getAllPlans();
    }

    @Operation(summary = "Return Plans by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Plan.class)
            )
    })
    public Optional<Plan> getPlanByID(@PathVariable("id") Integer planId) {
        return PlanService.getPlanByID(planId);
    }

    @Operation(summary = "Create Plans from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Plan.class)
            )
    })
    public Plan createPlan(@RequestBody Plan Plan){
        return PlanService.createPlan(Plan);
    }

    @Operation(summary = "Update specified Plans from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Plan.class)
            )
    })
    public Plan updatePlanByID(@PathVariable("id") Integer planId, @RequestBody Plan PlanParams){
        return PlanService.updatePlanByID(planId, PlanParams);
    }

    @Operation(summary = "Delete all Plans")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllPlan() {
        PlanService.deleteAllPlans();
    }

    @Operation(summary = "Delete specified Plan")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deletePlan(@PathVariable("id") Integer planId) {
        PlanService.deletePlanByID(planId);
    }
}

