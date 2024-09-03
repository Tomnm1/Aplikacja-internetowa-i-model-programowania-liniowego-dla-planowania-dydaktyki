package pl.poznan.put.planner_endpoints.Employee;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to communicate with DB
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
