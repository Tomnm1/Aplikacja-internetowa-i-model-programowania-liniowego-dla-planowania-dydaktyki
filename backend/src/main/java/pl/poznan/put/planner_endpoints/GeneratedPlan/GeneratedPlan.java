package pl.poznan.put.planner_endpoints.GeneratedPlan;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Classroom.Classroom;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Plan.Plan;
import pl.poznan.put.planner_endpoints.SlotsDay.SlotsDay;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;

/**
 * Data model for generatedPlans table
 */
@Entity
@Table(name = "generated_plans")
public class GeneratedPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "plan_id")
    public Plan plan;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "slot_day_id")
    public SlotsDay slotsDay;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "group_id")
    public Group group;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "teacher_id")
    public Teacher teacher;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "classroom_id")
    public Classroom classroom;
    @ManyToOne(cascade = CascadeType.PERSIST) // Unidirectional
    @JoinColumn(name = "subject_type__id")
    public SubjectType subjectType;
    @Column(name = "even_week")
    public Boolean isEvenWeek;
}
