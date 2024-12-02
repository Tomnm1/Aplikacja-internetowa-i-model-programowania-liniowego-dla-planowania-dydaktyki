package pl.poznan.put.planner_endpoints.Classroom;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import pl.poznan.put.planner_endpoints.Building.Building;
import pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes.ClassroomSubjectType;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

import java.util.HashMap;
import java.util.Map;

/**
 * Data model for classrooms table
 */
@Entity
@Table(name="classrooms")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "classroom_id")
    public Integer classroomID;
    @ManyToOne
    @JoinColumn(name = "building_id")
    public Building building;
    @Column(name = "code")
    public String code;
    @Column(name = "floor")
    public Short floor;
    @Column(name = "capacity")
    public Integer capacity;
    @Type(JsonType.class)
    @Column(name = "equipment", columnDefinition = "json")
    public Map<String, Boolean> equipment = new HashMap<>();

    public ClassroomSubjectType toClassroomSubjectType(SubjectType st){
        ClassroomSubjectType cst = new ClassroomSubjectType();
        cst.subjectType = st;
        cst.classroom = this;
        return cst;
    }
}