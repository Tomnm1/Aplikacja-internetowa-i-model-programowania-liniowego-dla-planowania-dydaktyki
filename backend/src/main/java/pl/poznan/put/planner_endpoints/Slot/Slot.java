package pl.poznan.put.planner_endpoints.Slot;

import jakarta.persistence.*;

import java.time.LocalTime;

/**
 * Data model for Slots table
 */
@Entity
@Table(name = "slots")
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    public Integer slotId;
    @Column(name = "start_time")
    public LocalTime startTime;
    @Column(name = "end_time")
    public LocalTime endTime;
}
