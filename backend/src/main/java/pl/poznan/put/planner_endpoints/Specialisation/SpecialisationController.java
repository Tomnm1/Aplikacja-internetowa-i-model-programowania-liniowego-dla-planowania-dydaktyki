package pl.poznan.put.planner_endpoints.Specialisation;

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
 * Controller for Specialisation resource
 */
@RestController
@RequestMapping("/specialisations")
public class SpecialisationController {
    @Autowired
    private pl.poznan.put.planner_endpoints.Specialisation.SpecialisationService SpecialisationService;

    @Operation(summary = "Return all Specialisations")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = Specialisation.class))
            )
    })
    public List<Specialisation> getAllSpecialisations() {
        return SpecialisationService.getAllSpecialisations();
    }

    @Operation(summary = "Return Specialisations by id")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Specialisation.class)
            )
    })
    public Optional<Specialisation> getSpecialisationByID(@PathVariable("id") Integer specialisationId) {
        return SpecialisationService.getSpecialisationByID(specialisationId);
    }

    @Operation(summary = "Create Specialisations from provided JSON")
    @PostMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Specialisation.class)
            )
    })
    public Specialisation createSpecialisation(@RequestBody Specialisation Specialisation){
        return SpecialisationService.createSpecialisation(Specialisation);
    }

    @Operation(summary = "Update specified Specialisations from provided JSON")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Specialisation.class)
            )
    })
    public Specialisation updateSpecialisationByID(@PathVariable("id") Integer specialisationId, @RequestBody Specialisation SpecialisationParams){
        return SpecialisationService.updateSpecialisationByID(specialisationId, SpecialisationParams);
    }

    @Operation(summary = "Delete all Specialisations")
    @DeleteMapping
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteAllSpecialisation() {
        SpecialisationService.deleteAllSpecialisations();
    }

    @Operation(summary = "Delete specified Specialisation")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = {
            @Content(schema = @Schema(hidden = true))
    })
    public void deleteSpecialisation(@PathVariable("id") Integer specialisationId) {
        SpecialisationService.deleteSpecialisationByID(specialisationId);
    }
}

