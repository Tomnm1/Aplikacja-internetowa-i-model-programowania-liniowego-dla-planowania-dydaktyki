package pl.poznan.put.planner_endpoints.Specialisation;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface SpecialisationRepository extends JpaRepository<Specialisation, Integer> {
}
