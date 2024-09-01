package pl.poznan.put.planner_endpoints.Sala;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for Sala resource
 */
@RestController
@RequestMapping("/sale")
public class SalaController {
    @Autowired
    private SalaService salaService;

    /**
     * Basic GET mapping
     * @return List of all Sala
     */
    @Operation(summary = "Return all Sala")
    @GetMapping
    public List<Sala> getAllSala() {
        return salaService.getAllSala();
    }

    @Operation(summary = "Return Sala by budynek and numer")
    @GetMapping("/{budynek}/{numer}")
    public Optional<Sala> getSalaByID(@PathVariable("budynek") String budynek, @PathVariable("numer") Integer numer) {
        return salaService.getSalaByID(new SalaCompositeKey(budynek, numer));
    }

    @Operation(summary = "Create Sala from provided JSON")
    @PostMapping
    public Sala createSala(@RequestBody Sala sala){
        return salaService.createSala(sala);
    }

    @Operation(summary = "Update specified Sala from provided JSON")
    @PutMapping("/{budynek}/{numer}")
    public Sala updateSalaByID(@PathVariable("budynek") String budynek, @PathVariable("numer") Integer numer, @RequestBody Sala salaParams){
        return salaService.updateSalaByID(new SalaCompositeKey(budynek, numer), salaParams);
    }

    @Operation(summary = "Delete all Sala")
    @DeleteMapping
    public void deleteAllUsers() {
        salaService.deleteAllSale();
    }

    @Operation(summary = "Delete specified Sala")
    @DeleteMapping("/{budynek}/{numer}")
    public void deleteUser(@PathVariable("budynek") String budynek, @PathVariable("numer") Integer numer) {
        salaService.deleteSalaByID(new SalaCompositeKey(budynek, numer));
    }
}
