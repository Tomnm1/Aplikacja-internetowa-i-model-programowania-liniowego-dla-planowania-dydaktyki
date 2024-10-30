package pl.poznan.put.planner_endpoints.Classroom;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import pl.poznan.put.planner_endpoints.Building.Building;

import java.util.HashMap;
import java.util.Map;

/**
 * Data model for classclassrooms table
 */
@Entity
@Table(name="classclassrooms")
public class Classroom {
    @Id
    @Column(name = "classroom_id")
    public Integer classroomID;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
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
}