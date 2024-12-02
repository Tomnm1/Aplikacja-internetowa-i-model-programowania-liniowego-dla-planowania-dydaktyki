package pl.poznan.put.planner_endpoints.Slot;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface SlotRepository extends JpaRepository<Slot, Integer> {
}
