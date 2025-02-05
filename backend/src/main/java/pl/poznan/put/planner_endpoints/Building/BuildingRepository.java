package pl.poznan.put.planner_endpoints.Building;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface BuildingRepository extends JpaRepository<Building, Integer> {
    Building findByCode(String code);
    boolean existsByCode(String code);
}
