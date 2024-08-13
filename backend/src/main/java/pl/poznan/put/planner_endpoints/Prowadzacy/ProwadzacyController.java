package pl.poznan.put.planner_endpoints.Prowadzacy;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for Sala resource
 */
@RestController
@RequestMapping("/prowadzacy")
public class ProwadzacyController {
    @Autowired
    private ProwadzacyService prowadzacyService;

    /**
     * Basic GET mapping
     * @return List of all Prowadzacy
     */
    @Operation(summary = "Return all Prowadzacy")
    @GetMapping
    public List<Prowadzacy> getAllProwadzacy() {
        return prowadzacyService.getAllProwadzacy();
    }

    @Operation(summary = "Return Prowadzacy by id")
    @GetMapping("/{id}")
    public Optional<Prowadzacy> getProwadzacyByID(@PathVariable("id") Integer id) {
        return prowadzacyService.getProwadzacyByID(id);
    }

    @Operation(summary = "Create Prowadzacy from provided JSON")
    @PostMapping
    public Prowadzacy createProwadzacy(@RequestBody Prowadzacy prowadzacy){
        return prowadzacyService.createProwadzacy(prowadzacy);
    }

    @Operation(summary = "Update specified Prowadzacy from provided JSON")
    @PutMapping("/{id}")
    public Prowadzacy updateProwadzacyByID(@PathVariable("id") Integer id, @RequestBody Prowadzacy prowadzacyParams){
        return prowadzacyService.updateProwadzacyByID(id, prowadzacyParams);
    }

    @Operation(summary = "Delete all Prowadzacy")
    @DeleteMapping
    public void deleteAllProwadzacy() {
        prowadzacyService.deleteAllProwadzacy();
    }

    @Operation(summary = "Delete specified Prowadzacy")
    @DeleteMapping("/{id}")
    public void deleteProwadzacy(@PathVariable("id") Integer id) {
        prowadzacyService.deleteProwadzacyByID(id);
    }
}
