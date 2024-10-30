package pl.poznan.put.planner_endpoints.FieldOfStudy;

import jakarta.persistence.*;

/**
 * Data model for fieldOfStudys table
 */
@Entity
@Table(name = "fieldOfStudys")
public class FieldOfStudy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_of_study_id")
    public Integer fieldOfStudyId;
    @Column(name = "name")
    public String name;
}
