package pl.poznan.put.planner_endpoints.Sala;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfejs do komunikacji z bazą
 */
public interface SalaRepository extends JpaRepository<Sala, SalaCompositeKey> {
}
