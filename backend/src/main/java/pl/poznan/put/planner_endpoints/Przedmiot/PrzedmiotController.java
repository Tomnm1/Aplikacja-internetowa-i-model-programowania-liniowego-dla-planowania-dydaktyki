package pl.poznan.put.planner_endpoints.Przedmiot;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for Przedmioty resource
 */
@RestController
@RequestMapping("/przedmioty")
public class PrzedmiotController {
    @Autowired
    private PrzedmiotService przedmiotService;

    /**
     * Basic GET mapping
     * @return List of all Przedmiot
     */
    @Operation(summary = "Return all Przedmiot")
    @GetMapping
    public List<Przedmiot> getAllPrzedmiot() {
        return przedmiotService.getAllPrzedmiot();
    }

    @Operation(summary = "Return Przedmiot by id")
    @GetMapping("/{id}")
    public Optional<Przedmiot> getPrzedmiotByID(@PathVariable("id") Integer id) {
        return przedmiotService.getPrzedmiotByID(id);
    }

    @Operation(summary = "Create Przedmiot from provided JSON")
    @PostMapping
    public Przedmiot createPrzedmiot(@RequestBody Przedmiot przedmiot){
        return przedmiotService.createPrzedmiot(przedmiot);
    }

    @Operation(summary = "Update specified Przedmiot from provided JSON")
    @PutMapping("/{id}")
    public Przedmiot updatePrzedmiotByID(@PathVariable("id") Integer id, @RequestBody Przedmiot przedmiotParams){
        return przedmiotService.updatePrzedmiotByID(id, przedmiotParams);
    }

    @Operation(summary = "Delete all Przedmiot")
    @DeleteMapping
    public void deleteAllPrzedmioty() {
        przedmiotService.deleteAllPrzedmioty();
    }

    @Operation(summary = "Delete specified Przedmiot")
    @DeleteMapping("/{id}")
    public void deletePrzedmiot(@PathVariable("id") Integer id) {
        przedmiotService.deletePrzedmiotByID(id);
    }
}
