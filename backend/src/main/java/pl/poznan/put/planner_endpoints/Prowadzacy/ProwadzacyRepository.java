package pl.poznan.put.planner_endpoints.Prowadzacy;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfejs do komunikacji z bazą
 */
public interface ProwadzacyRepository extends JpaRepository<Prowadzacy, Integer> {
}
