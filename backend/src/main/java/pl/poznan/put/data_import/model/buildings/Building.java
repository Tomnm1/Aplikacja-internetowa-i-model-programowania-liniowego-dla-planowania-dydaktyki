package pl.poznan.put.data_import.model.buildings;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "budynek")
@XmlAccessorType(XmlAccessType.FIELD)
public class Building {
    @XmlAttribute(name = "id")
    private int id;

    @XmlElement(name = "kod")
    private String code;

    @XmlElement(name = "nazwa")
    private String name;

    @XmlElement(name = "kampus")
    private Campus campus;

//    @XmlElementWrapper(name = "jednostkiOrganizacyjne")
//    @XmlElement(name = "jednostkaOrganizacyjna")
//    private List<JednostkaOrganizacyjna> jednostkiOrganizacyjne;

    @XmlElement(name = "sala")
    private List<Classroom> classrooms;

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
