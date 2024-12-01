package pl.poznan.put.planner_endpoints.SlotsDay;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interface to communicate with DB
 */
public interface SlotsDayRepository extends JpaRepository<SlotsDay, Integer> {
    List<SlotsDay> findAllByDayOrderBySlotStartTime(Day day);
}
