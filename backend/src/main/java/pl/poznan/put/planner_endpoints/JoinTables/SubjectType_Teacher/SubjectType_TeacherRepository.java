package pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.List;

public interface SubjectType_TeacherRepository extends JpaRepository<SubjectType_Teacher, Integer> {
}
