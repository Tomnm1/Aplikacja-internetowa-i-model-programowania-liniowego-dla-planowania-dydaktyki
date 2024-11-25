package pl.poznan.put.planner_endpoints.SubjectTypeTeacher;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;

@Entity
@Table(name = "subject_type_teacher")
public class SubjectTypeTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_type_teacher_id")
    public Integer subjectTypeTeacherId;
    @ManyToOne
    @JoinColumn(name = "subject_type_id")
    public SubjectType subjectType;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    public Teacher teacher;
    @Column(name = "num_hours")
    public Integer numHours;
}
