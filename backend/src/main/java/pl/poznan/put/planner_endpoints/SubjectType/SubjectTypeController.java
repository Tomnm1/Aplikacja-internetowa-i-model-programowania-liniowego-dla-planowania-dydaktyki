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
@RestController
@RequestMapping("/subjectTypes")
public class SubjectTypeController {
    @Autowired
    private SubjectTypeService subjectTypeService;

    @Operation(summary = "Return all subjectTypes")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = SubjectTypeDTO.class))
            )
    })
    public List<SubjectTypeDTO> getAllSubjectType() {
        return subjectTypeService.getAllsubjectTypeDTO();
    }

    @Operation(summary = "Return all subjectTypes with subject id")
    @GetMapping("/subject/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = SubjectTypeDTO.class))
            )
    })
    public List<SubjectTypeDTO> getAllSubjectTypeBySubjectID(@PathVariable("id") Integer id) {
        return subjectTypeService.getAllsubjectTypeBySubjectIDDTO(id);
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
    public SubjectTypeDTO createSubjectType(@RequestBody SubjectTypeDTO subjectTypeDTO){
        return subjectTypeService.createsubjectTypeDTO(subjectTypeDTO);
    }

    @Operation(summary = "Update specified SubjectType from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SubjectType.class)
            )
    })
    public SubjectTypeDTO updateSubjectTypeByID(@PathVariable("id") Integer id, @RequestBody SubjectTypeDTO subjectTypeParams){
        return subjectTypeService.updatesubjectTypeDTOByID(id, subjectTypeParams);
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
