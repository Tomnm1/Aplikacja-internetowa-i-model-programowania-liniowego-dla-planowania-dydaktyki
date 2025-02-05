package pl.poznan.put.data_import.model.buildings;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sala")
public class Classroom {
    @XmlAttribute(name = "id")
    private int id;

    @XmlElement(name = "numer")
    private String number;

    @XmlElement(name = "pojemnosc")
    private int capacity;

    @XmlElement(name = "USOSid")
    private int usosId;

//    @XmlElement(name = "jednostkaOrg")
//    private int jednostkaOrg;

    @XmlElement(name = "metraz")
    private String area;

    @XmlElement(name = "czyDydaktyczna")
    private String isDidactic;

    @XmlElement(name = "atrybutSali")
    private List<ClassroomAttribute> classroomAttributes;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getIsDidactic() {
        return isDidactic;
    }

    public String getArea() {
        return area;
    }

    public List<ClassroomAttribute> getClassroomAttributes() {
        return classroomAttributes;
    }

    public String getNumber() {
        return number;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setClassroomAttributes(List<ClassroomAttribute> classroomAttributes) {
        this.classroomAttributes = classroomAttributes;
    }

    public void setIsDidactic(String didactic) {
        isDidactic = didactic;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setUsosId(int usosId) {
        this.usosId = usosId;
    }

    public int getUsosId() {
        return usosId;
    }

    public int getCapacity() {
        return capacity;
    }
}
