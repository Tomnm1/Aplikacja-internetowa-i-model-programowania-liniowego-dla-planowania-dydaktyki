package pl.poznan.put.planner_endpoints.Plan;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Data model for plans table
 */
@Entity
@Table(name = "plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    public Integer planId;
    @Column(name = "name")
    public String name;
    @Column(name = "creation_date")
    public LocalDateTime creationDate;
}
