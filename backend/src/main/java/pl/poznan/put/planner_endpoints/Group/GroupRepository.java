package pl.poznan.put.planner_endpoints.Group;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface GroupRepository  extends JpaRepository<Group, Integer> {
}
