package pl.poznan.put.data_import.model.subjects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Subject {

    @XmlAttribute(name = "id")
    private int id;

    @XmlElement(name = "nazwa")
    private String name;

    @XmlElement(name = "kod")
    private String code;

    @XmlElement(name = "jednostkaOrganizacyjna")
    private OrganizationalUnitReference organizationalUnit;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public OrganizationalUnitReference getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(OrganizationalUnitReference organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }
}