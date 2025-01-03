package pl.poznan.put.planner_endpoints.Teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.util.Optional;


/**
 * Interface to communicate with DB
 */
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher findByFirstNameAndLastNameAndSecondName(String firstName, String lastName, String secondName);
    Teacher findByFirstNameAndLastName(String firstName, String lastName);
    Teacher findByUsosId(int usosId);
    Teacher findByInnerId(int innerId);

    Optional<Teacher> findTeacherByEmail(String email);

    @Query(value = "SELECT * FROM teachers WHERE LENGTH(preferences::text) > 2", nativeQuery = true)
    List<Teacher> findAllTeachersWithPreferences();

    @Query(value = "SELECT * FROM teachers ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Teacher findRandomTeacher();

}
