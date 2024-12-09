package pl.poznan.put.PlanningProgress;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/planningProgress")
public class PlanningProgressController {

    private final PlanningProgressService planningProgressService;

    public PlanningProgressController(PlanningProgressService planningProgressService) {
        this.planningProgressService = planningProgressService;
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<PlanningProgressResponse> getProgress(@PathVariable String jobId) {
        Optional<PlanningProgress> progressOpt = planningProgressService.getProgress(jobId);

        if (progressOpt.isPresent()) {
            PlanningProgress progress = progressOpt.get();
            return ResponseEntity.ok(new PlanningProgressResponse(progress.progress (), progress.status ().name()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
        record PlanningProgressResponse(int progress, String status) {
    }
}
