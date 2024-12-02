package pl.poznan.put.planner_endpoints.Semester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.poznan.put.planner_endpoints.Specialisation.Specialisation;

import java.util.List;

/**
 * Interface to communicate with DB
 */
public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    Semester findByNumberAndSpecialisation(String number, Specialisation specialisation);
    boolean existsByNumberAndSpecialisation(String number, Specialisation specialisation);
    @Query(value = "SELECT s.semester_id,s.number,s.specialisation_id,count(g.group_id),s.typ \n" +
        "FROM semesters as s \n" +
        "LEFT JOIN groups g ON g.semester_id = s.semester_id AND g.group_type = 'laboratoria'\n" +
        "group by s.semester_id;", nativeQuery = true)
    List<Object[]> findAllSemestersDTO();


    @Modifying
    @Query(value = "CALL defineGroup(:param1, :param2)", nativeQuery = true)
    void defineGroup(@Param("param1") Integer groupCount, @Param("param2") Integer SemesterId);

}
