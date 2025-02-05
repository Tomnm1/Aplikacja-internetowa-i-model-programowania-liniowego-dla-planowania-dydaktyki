package pl.poznan.put.planner_endpoints.GeneratedPlan;

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
 * Controller for GeneratedPlan resource
 */
@RestController
@RequestMapping("/generatedPlans")
public class GeneratedPlanController {
    @Autowired
    private pl.poznan.put.planner_endpoints.GeneratedPlan.GeneratedPlanService GeneratedPlanService;

    @Operation(summary = "Return all GeneratedPlans")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = GeneratedPlan.class))
            )
    })
    public List<GeneratedPlanDTO> getAllGeneratedPlans() {
        return GeneratedPlanService.getAllGeneratedPlansDTO();
    }

    @Operation(summary = "Return all GeneratedPlans by plan ID")
    @GetMapping("/plans/{planId}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = GeneratedPlanDTO.class))
            )
    })
    public List<GeneratedPlanDTO> getAllGeneratedPlans(@PathVariable("planId") Integer planId) {
        return GeneratedPlanService.getAllGeneratedPlansById(planId);
    }

    @Operation(summary = "Return GeneratedPlans filtered by Teacher ID")
    @GetMapping("{planId}/teacher/{teacherId}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = GeneratedPlanDTO.class))
            )
    })
    public List<GeneratedPlanDTO> getGeneratedPlansByTeacherId(@PathVariable("teacherId") Integer teacherId, @PathVariable("planId") Integer planId ) {
        return GeneratedPlanService.getGeneratedPlansByTeacherId(teacherId, planId);
    }

    @Operation(summary = "Return GeneratedPlans filtered by Classroom ID")
    @GetMapping("{planId}/classroom/{classroomID}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = GeneratedPlanDTO.class))
            )
    })
    public List<GeneratedPlanDTO> getGeneratedPlansByClassroomId(@PathVariable("classroomID") Integer classroomID, @PathVariable("planId") Integer planId) {
        return GeneratedPlanService.getGeneratedPlansByClassroomId(classroomID, planId);
    }

    @Operation(summary = "Return GeneratedPlans filtered by semesterId")
    @GetMapping("{planId}/semester/{semesterId}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = GeneratedPlanDTO.class))
            )
    })
    public List<GeneratedPlanDTO> findAllByGroupSemesterSemesterId(@PathVariable("semesterId") Integer semesterId, @PathVariable("planId") Integer planId) {
        return GeneratedPlanService.findAllByGroupSemesterSemesterId(semesterId, planId);
    }

    @Operation(summary = "Return GeneratedPlans by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GeneratedPlan.class)
            )
    })
    public Optional<GeneratedPlan> getGeneratedPlanByID(@PathVariable("id") Integer generatedPlanId) {
        return GeneratedPlanService.getGeneratedPlanByID(generatedPlanId);
    }

    @Operation(summary = "Create GeneratedPlans from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GeneratedPlan.class)
            )
    })
    public GeneratedPlan createGeneratedPlan(@RequestBody GeneratedPlan GeneratedPlan){
        return GeneratedPlanService.createGeneratedPlan(GeneratedPlan);
    }

    @Operation(summary = "Update specified GeneratedPlans from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GeneratedPlan.class)
            )
    })
    public GeneratedPlan updateGeneratedPlanByID(@PathVariable("id") Integer generatedPlanId, @RequestBody GeneratedPlan GeneratedPlanParams){
        return GeneratedPlanService.updateGeneratedPlanByID(generatedPlanId, GeneratedPlanParams);
    }

    @Operation(summary = "Delete all GeneratedPlans")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllGeneratedPlan() {
        GeneratedPlanService.deleteAllGeneratedPlans();
    }

    @Operation(summary = "Delete specified GeneratedPlan")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteGeneratedPlan(@PathVariable("id") Integer generatedPlanId) {
        GeneratedPlanService.deleteGeneratedPlanByID(generatedPlanId);
    }
}

