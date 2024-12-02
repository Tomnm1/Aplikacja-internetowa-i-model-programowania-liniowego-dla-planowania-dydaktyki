package pl.poznan.put.planner_endpoints.ClassroomsSubjectTypes;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.Classroom.Classroom;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;

@Entity
@Table(name = "classrooms_subject_types")
public class ClassroomSubjectType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer id;
    @ManyToOne
    @JoinColumn(name = "classroom_id")
    public Classroom classroom;
    @ManyToOne
    @JoinColumn(name = "subject_type_id")
    public SubjectType subjectType;

    public Classroom getClassroom() {
        return classroom;
    }
}
