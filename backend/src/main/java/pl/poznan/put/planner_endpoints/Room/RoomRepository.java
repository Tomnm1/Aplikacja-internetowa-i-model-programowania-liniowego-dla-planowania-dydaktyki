package pl.poznan.put.planner_endpoints.Room;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface RoomRepository extends JpaRepository<Room, RoomCompositeKey> {
}
