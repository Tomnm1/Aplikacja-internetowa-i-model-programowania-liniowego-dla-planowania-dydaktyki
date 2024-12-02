package pl.poznan.put.planner_endpoints.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Interface to communicate with DB
 */
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher findByFirstNameAndLastNameAndSecondName(String firstName, String lastName, String secondName);
    Teacher findByFirstNameAndLastName(String firstName, String lastName);
    Teacher findByUsosId(int usosId);
    Teacher findByInnerId(int innerId);

    @Query(value = "SELECT * FROM teachers ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Teacher findRandomTeacher();
}
