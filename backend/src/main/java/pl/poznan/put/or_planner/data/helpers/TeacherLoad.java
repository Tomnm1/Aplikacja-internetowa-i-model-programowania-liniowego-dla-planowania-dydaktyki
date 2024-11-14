package pl.poznan.put.or_planner.data.helpers;

import java.util.List;

public class TeacherLoad {
    private String teacher;
    private List<TeacherLoadSubject> teacherLoadSubjectList;

    public TeacherLoad(String teacher, List<TeacherLoadSubject> teacherLoadSubjectList){
        this.teacher = teacher;
        this.teacherLoadSubjectList = teacherLoadSubjectList;
    }

    public List<TeacherLoadSubject> getTeacherLoadSubjectList() {
        return teacherLoadSubjectList;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setTeacherLoadSubjectList(List<TeacherLoadSubject> teacherLoadSubjectList) {
        this.teacherLoadSubjectList = teacherLoadSubjectList;
    }
}
