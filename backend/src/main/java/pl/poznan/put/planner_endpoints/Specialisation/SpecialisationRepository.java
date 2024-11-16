package pl.poznan.put.planner_endpoints.Specialisation;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.FieldOfStudy.FieldOfStudy;

/**
 * Interface to communicate with DB
 */
public interface SpecialisationRepository extends JpaRepository<Specialisation, Integer> {
    Specialisation findByNameAndCycleAndFieldOfStudy(String name, Cycle cycle, FieldOfStudy fieldOfStudy);
}
