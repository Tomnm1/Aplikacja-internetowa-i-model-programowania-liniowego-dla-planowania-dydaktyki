package pl.poznan.put.data_import.model.subjects;

public class TeacherWithInnerId {
    private String innerId;
    private int numHours;

    public TeacherWithInnerId(String innerId, int numHours) {
        this.innerId = innerId;
        this.numHours = numHours;
    }

    public String getInnerId() {
        return innerId;
    }

    public void setInnerId(String innerId) {
        this.innerId = innerId;
    }

    public int getNumHours() {
        return numHours;
    }

    public void setNumHours(int numHours) {
        this.numHours = numHours;
    }
}
