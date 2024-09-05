package pl.poznan.put.planner_endpoints.Subject;


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
 * Controller for Subject resource
 */
@RestController // TODO use http response builders to create success and fail responses
@RequestMapping("/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @Operation(summary = "Return all Subjects")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Subject.class))
            )
    })
    public List<Subject> getAllSubject() {
        return subjectService.getAllSubject();
    }

    @Operation(summary = "Return Subject by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Subject.class)
            )
    })
    public Optional<Subject> getSubjectByID(@PathVariable("id") Integer id) {
        return subjectService.getSubjectByID(id);
    }

    @Operation(summary = "Create Subject from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Subject.class)
            )
    })
    // TODO add check to fail if entity already exists
    public Subject createSubject(@RequestBody Subject subject){
        return subjectService.createSubject(subject);
    }

    @Operation(summary = "Update specified Subject from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Subject.class)
            )
    })
    public Subject updateSubjectByID(@PathVariable("id") Integer id, @RequestBody Subject subjectParams){
        return subjectService.updateSubjectByID(id, subjectParams);
    }

    @Operation(summary = "Delete all Subjects")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllSubjects() {
        subjectService.deleteAllSubjects();
    }

    @Operation(summary = "Delete specified Subject")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteSubject(@PathVariable("id") Integer id) {
        subjectService.deleteSubjectByID(id);
    }
}
