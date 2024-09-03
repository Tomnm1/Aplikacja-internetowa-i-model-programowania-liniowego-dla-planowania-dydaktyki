package pl.poznan.put.planner_endpoints.Building;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Data model for buildings table
 */
@Entity
@Table(name = "budynki")
public class Building {
    @Id
    @Column(name = "budynek")
    public String building;
}
