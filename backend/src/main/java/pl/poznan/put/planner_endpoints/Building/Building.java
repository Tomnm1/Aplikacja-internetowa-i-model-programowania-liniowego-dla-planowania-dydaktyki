package pl.poznan.put.planner_endpoints.Building;

import jakarta.persistence.*;

/**
 * Data model for buildings table
 */
@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    public Integer buildingId;
    @Column(name = "code")
    public String code;
}
