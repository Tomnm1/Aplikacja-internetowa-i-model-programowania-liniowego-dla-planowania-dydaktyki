package pl.poznan.put.PlanningProgress;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlanningProgressService {
    private final Map<String, PlanningProgress> progressMap = new ConcurrentHashMap<>();

    public void setProgress(String jobId, int progress, PlanningStatus status) {
        progressMap.put(jobId, new PlanningProgress(jobId, progress, status));
    }

    public Optional<PlanningProgress> getProgress(String jobId) {
        return Optional.ofNullable(progressMap.get(jobId));
    }

    public void removeProgress(String jobId) {
        progressMap.remove(jobId);
    }
}
