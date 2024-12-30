package pl.poznan.put.planner_endpoints.GeneratedPlan;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Classroom.Classroom;
import pl.poznan.put.planner_endpoints.Group.Group;
import pl.poznan.put.planner_endpoints.Plan.Plan;
import pl.poznan.put.planner_endpoints.SlotsDay.SlotsDay;
import pl.poznan.put.planner_endpoints.SubjectType.ClassTypeOwn;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.Teacher.Degree;
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
    @ManyToOne
    @JoinColumn(name = "plan_id")
    public Plan plan;
    @ManyToOne
    @JoinColumn(name = "slot_day_id")
    public SlotsDay slotsDay;
    @ManyToOne
    @JoinColumn(name = "group_id")
    public Group group;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    public Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    public Classroom classroom;
    @ManyToOne
    @JoinColumn(name = "subject_type_id")
    public SubjectType subjectType;
    @Column(name = "even_week")
    public Boolean isEvenWeek;

    public GeneratedPlanDTO convertToDTO() {
        GeneratedPlanDTO dto = new GeneratedPlanDTO();
        dto.id = this.id;
        dto.isEvenWeek = this.isEvenWeek;
        dto.planId = this.plan.planId;
        dto.slotsDay = this.slotsDay;
        dto.groupId = this.group.id;
        dto.groupCode = this.group.code;
        dto.teacherId = this.teacher.id;
        dto.teacherFirstName = this.teacher.firstName;
        dto.teacherLastName = this.teacher.lastName;
        dto.degree = this.teacher.degree;
        dto.classroomId = this.classroom.classroomID;
        dto.classroomCode = this.classroom.code;
        dto.subjectTypeId = this.subjectType.subjectTypeId;
        dto.subjectName = this.subjectType.subject.name;
        dto.classTypeOwn = this.subjectType.type;
        dto.specializationName = this.group.semester.specialisation.name;
        dto.specializationSemester = this.group.semester.number;
        dto.specializationId = this.group.semester.semesterId;
        dto.fieldOfStudyName = this.group.semester.specialisation.fieldOfStudy.name;
        dto.buildingName = this.classroom.building.code;
        return dto;
    }
}
