package pl.poznan.put.or_planner.insert;

public class PlannedSlot {
    private final String slotDayId;
    private final String groupId;
    private final String teacherId;
    private final String classroomId;
    private final String subjectTypeId;
    private final boolean evenWeek;

    public PlannedSlot(String slotDayId, String groupId, String teacherId, String classroomId, String subjectTypeId,
                       boolean evenWeek){
        this.slotDayId = slotDayId;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.classroomId = classroomId;
        this.subjectTypeId = subjectTypeId;
        this.evenWeek = evenWeek;
    }

    public Integer getSlotDayId(){
        return Integer.valueOf(this.slotDayId);
    }

    public Integer getGroupId(){
        return Integer.valueOf(this.groupId);
    }

    public Integer getTeacherId(){
        return Integer.valueOf(this.teacherId);
    }

    public Integer getClassroomId(){
        return Integer.valueOf(this.classroomId);
    }

    public Integer getSubjectTypeId(){
        return Integer.valueOf(this.subjectTypeId);
    }

    public boolean getEvenWeek(){
        return this.evenWeek;
    }
}