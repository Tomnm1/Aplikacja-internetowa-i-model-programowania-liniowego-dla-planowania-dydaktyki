package pl.poznan.put.planner_endpoints.Subgroup;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface SubgroupRepository  extends JpaRepository<Subgroup, Integer> {
}
