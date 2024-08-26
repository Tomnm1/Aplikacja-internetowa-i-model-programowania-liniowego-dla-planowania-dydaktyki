package pl.poznan.put.xml_reader.model.buildings;

import jakarta.xml.bind.annotation.*;

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
    private String classroomAttribute;

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

    public String getClassroomAttribute() {
        return classroomAttribute;
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

    public void setClassroomAttribute(String classroomAttribute) {
        this.classroomAttribute = classroomAttribute;
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
