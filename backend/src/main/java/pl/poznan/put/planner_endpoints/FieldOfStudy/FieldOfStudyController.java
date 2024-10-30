package pl.poznan.put.planner_endpoints.FieldOfStudy;

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
 * Controller for FieldOfStudy resource
 */
@RestController
@RequestMapping("/fieldOfStudys")
public class FieldOfStudyController {
    @Autowired
    private pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudyService FieldOfStudyService;

    @Operation(summary = "Return all FieldOfStudys")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = FieldOfStudy.class))
            )
    })
    public List<FieldOfStudy> getAllFieldOfStudys() {
        return FieldOfStudyService.getAllFieldOfStudys();
    }

    @Operation(summary = "Return FieldOfStudys by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FieldOfStudy.class)
            )
    })
    public Optional<FieldOfStudy> getFieldOfStudyByID(@PathVariable("id") Integer fieldOfStudyId) {
        return FieldOfStudyService.getFieldOfStudyByID(fieldOfStudyId);
    }

    @Operation(summary = "Create FieldOfStudys from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FieldOfStudy.class)
            )
    })
    public FieldOfStudy createFieldOfStudy(@RequestBody FieldOfStudy FieldOfStudy){
        return FieldOfStudyService.createFieldOfStudy(FieldOfStudy);
    }

    @Operation(summary = "Update specified FieldOfStudys from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FieldOfStudy.class)
            )
    })
    public FieldOfStudy updateFieldOfStudyByID(@PathVariable("id") Integer fieldOfStudyId, @RequestBody FieldOfStudy FieldOfStudyParams){
        return FieldOfStudyService.updateFieldOfStudyByID(fieldOfStudyId, FieldOfStudyParams);
    }

    @Operation(summary = "Delete all FieldOfStudys")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllFieldOfStudy() {
        FieldOfStudyService.deleteAllFieldOfStudys();
    }

    @Operation(summary = "Delete specified FieldOfStudy")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteFieldOfStudy(@PathVariable("id") Integer fieldOfStudyId) {
        FieldOfStudyService.deleteFieldOfStudyByID(fieldOfStudyId);
    }
}

