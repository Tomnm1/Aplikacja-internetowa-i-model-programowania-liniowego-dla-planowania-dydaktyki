package pl.poznan.put.planner_endpoints.Group;

import jakarta.persistence.*;

/**
 * Data model fot groups table
 */
@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @Column(name = "code")
    public String code;
}
