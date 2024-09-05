package pl.poznan.put.planner_endpoints.Room;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import pl.poznan.put.planner_endpoints.Building.Building;
import pl.poznan.put.planner_endpoints.Employee.Employee;

import java.util.HashMap;
import java.util.Map;

/**
 * Data model for rooms table
 */
@Entity
@Table(name="sale")
@IdClass(RoomCompositeKey.class)
public class Room {
    @Id
    @Column(name = "numer")
    public Integer number;
    @Id
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "budynek")
    public Building building;
    @Column(name = "pietro")
    public Short floor;
    @Column(name = "liczba_miejsc")
    public Integer numOfSeats;
    @Column(name = "typ")
    public String type;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "opiekun")
    public Employee caretaker;
    @Type(JsonType.class)
    @Column(name = "wyposazenie", columnDefinition = "json")
    public Map<String, Boolean> equipment = new HashMap<>();;
}