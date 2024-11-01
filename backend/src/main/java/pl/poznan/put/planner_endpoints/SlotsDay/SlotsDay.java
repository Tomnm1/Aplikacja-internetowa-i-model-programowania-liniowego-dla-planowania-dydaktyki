package pl.poznan.put.planner_endpoints.SlotsDay;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.poznan.put.planner_endpoints.Slot.Slot;

/**
 * Data model for SlotsDays table
 */
@Entity
@Table(name = "slots_days")
public class SlotsDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slots_days_id")
    public Integer SlotsDayId;
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "day")
    public Day code; // TODO typ????
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "slot_id")
    public Slot slot;
}
