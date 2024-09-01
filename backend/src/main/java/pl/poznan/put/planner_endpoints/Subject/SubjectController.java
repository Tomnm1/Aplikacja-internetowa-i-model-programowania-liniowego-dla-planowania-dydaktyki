package pl.poznan.put.planner_endpoints.Subject;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for Subject resource
 */
@RestController
@RequestMapping("/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @Operation(summary = "Return all Subjects")
    @GetMapping
    public List<Subject> getAllSubject() {
        return subjectService.getAllSubject();
    }

    @Operation(summary = "Return Subject by id")
    @GetMapping("/{id}")
    public Optional<Subject> getSubjectByID(@PathVariable("id") Integer id) {
        return subjectService.getSubjectByID(id);
    }

    @Operation(summary = "Create Subject from provided JSON")
    @PostMapping
    // TODO add check to fail if entity already exists
    public Subject createSubject(@RequestBody Subject subject){
        return subjectService.createSubject(subject);
    }

    @Operation(summary = "Update specified Subject from provided JSON")
    @PutMapping("/{id}")
    public Subject updateSubjectByID(@PathVariable("id") Integer id, @RequestBody Subject subjectParams){
        return subjectService.updateSubjectByID(id, subjectParams);
    }

    @Operation(summary = "Delete all Subjects")
    @DeleteMapping
    public void deleteAllSubjects() {
        subjectService.deleteAllSubjects();
    }

    @Operation(summary = "Delete specified Subject")
    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable("id") Integer id) {
        subjectService.deleteSubjectByID(id);
    }
}
