package pl.poznan.put.planner_endpoints.Semester;

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
 * Controller for Semester resource
 */
@RestController
@RequestMapping("/semesters")
public class SemesterController {
    @Autowired
    private pl.poznan.put.planner_endpoints.Semester.SemesterService SemesterService;

    @Operation(summary = "Return all Semesters")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Semester.class))
            )
    })
    public List<Semester> getAllSemesters() {
        return SemesterService.getAllSemesters();
    }

    @Operation(summary = "Return Semesters by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Semester.class)
            )
    })
    public Optional<Semester> getSemesterByID(@PathVariable("id") Integer semesterId) {
        return SemesterService.getSemesterByID(semesterId);
    }

    @Operation(summary = "Create Semesters from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Semester.class)
            )
    })
    public Semester createSemester(@RequestBody Semester Semester){
        return SemesterService.createSemester(Semester);
    }

    @Operation(summary = "Update specified Semesters from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Semester.class)
            )
    })
    public Semester updateSemesterByID(@PathVariable("id") Integer semesterId, @RequestBody Semester SemesterParams){
        return SemesterService.updateSemesterByID(semesterId, SemesterParams);
    }

    @Operation(summary = "Delete all Semesters")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllSemester() {
        SemesterService.deleteAllSemesters();
    }

    @Operation(summary = "Delete specified Semester")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteSemester(@PathVariable("id") Integer semesterId) {
        SemesterService.deleteSemesterByID(semesterId);
    }
}

