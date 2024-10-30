package pl.poznan.put.planner_endpoints.Building;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Data model for buildings table
 */
@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @Column(name = "building_id")
    public Integer buildingId;
    @Column(name = "code")
    public String code;
}
