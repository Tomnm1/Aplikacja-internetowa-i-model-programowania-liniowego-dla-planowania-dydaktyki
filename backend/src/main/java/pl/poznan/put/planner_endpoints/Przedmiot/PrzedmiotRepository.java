package pl.poznan.put.planner_endpoints.Przedmiot;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfejs do komunikacji z bazą
 */
public interface PrzedmiotRepository extends JpaRepository<Przedmiot, Integer> {
}
