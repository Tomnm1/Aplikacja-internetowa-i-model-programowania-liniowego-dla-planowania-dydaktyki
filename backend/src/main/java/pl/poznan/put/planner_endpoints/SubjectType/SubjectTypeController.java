package pl.poznan.put.planner_endpoints.SubjectType;


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
 * Controller for SubjectType resource
 */
@RestController // TODO use http response builders to create success and fail responses
@RequestMapping("/subjectTypes")
public class SubjectTypeController {
    @Autowired
    private SubjectTypeService subjectTypeService;

    @Operation(summary = "Return all subjectTypes")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = SubjectType.class))
            )
    })
    public List<SubjectType> getAllSubjectType() {
        return subjectTypeService.getAllsubjectType();
    }

    @Operation(summary = "Return SubjectType by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SubjectType.class)
            )
    })
    public Optional<SubjectType> getSubjectTypeByID(@PathVariable("id") Integer id) {
        return subjectTypeService.getsubjectTypeByID(id);
    }

    @Operation(summary = "Create SubjectType from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SubjectType.class)
            )
    })
    // TODO add check to fail if entity already exists
    public SubjectType createSubjectType(@RequestBody SubjectType subjectType){
        return subjectTypeService.createsubjectType(subjectType);
    }

    @Operation(summary = "Update specified SubjectType from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SubjectType.class)
            )
    })
    public SubjectType updateSubjectTypeByID(@PathVariable("id") Integer id, @RequestBody SubjectType subjectTypeParams){
        return subjectTypeService.updatesubjectTypeByID(id, subjectTypeParams);
    }

    @Operation(summary = "Delete all subjectTypes")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllSubjectTypes() {
        subjectTypeService.deleteAllsubjectTypes();
    }

    @Operation(summary = "Delete specified SubjectType")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteSubjectType(@PathVariable("id") Integer id) {
        subjectTypeService.deletesubjectTypeByID(id);
    }
}
