package pl.poznan.put.planner_endpoints.JoinTables.SubjectType_Teacher;

import jakarta.persistence.*;
import pl.poznan.put.planner_endpoints.SubjectType.SubjectType;
import pl.poznan.put.planner_endpoints.Teacher.Teacher;

@Entity
@Table(name="subject_type_teacher")
public class SubjectType_Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_type_teacher_id")
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    public Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "subject_type_id")
    public SubjectType subjectType;

    @Column(name = "num_hours")
    public Integer numHours;

    public Integer getSubjetTyprId() {
        return subjectType.subjectTypeId;
    }

    public Integer getTeacherId() {
        return teacher.id;
    }

    public SubjectTypeTeacherDTO toSubjectTypeTeacherDTO() {
        SubjectTypeTeacherDTO dto = new SubjectTypeTeacherDTO();
        dto.id = id;
        dto.teacherId = teacher.id;
        dto.teacherFirstName = teacher.firstName;
        dto.teacherLastName = teacher.lastName;
        dto.subjectTypeId = subjectType.subjectTypeId;
        dto.numHours = this.numHours;
        return dto;
    }

    @Override
    public String toString() {
        return id.toString();
        //return id.toString() + " - " + teacher.id.toString() + " - " + subjectType.subjectTypeId.toString();
    }

    @Override
    public boolean equals(Object obj) {
        SubjectType_Teacher stt = (SubjectType_Teacher) obj;
        if (stt.id == null) return false;
        return (stt.id == this.id || stt.id == 0);
    }
}