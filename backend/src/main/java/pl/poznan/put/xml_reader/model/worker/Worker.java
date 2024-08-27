package pl.poznan.put.xml_reader.model.worker;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pracownik", propOrder = {
        "name",          // "imie" in XML
        "surname",       // "nazwisko" in XML
        "workerId",      // "prac_id" in XML
        "titleDegree",   // "tytulyStopnie" in XML
        "funkcja"        // "funkcja" in XML
})
@XmlRootElement(name = "pracownik")
public class Worker {

    @XmlAttribute(name = "id")
    private int id;

    @XmlAttribute(name = "obcy")
    private boolean foreign;

    @XmlElement(name = "imie")
    private String name;

    @XmlElement(name = "nazwisko")
    private String surname;

    @XmlElement(name = "prac_id")
    private int workerId;

    @XmlElement(name = "tytulyStopnie")
    private String titleDegree;

    @XmlElement(name = "funkcja")
    private WorkerFunction funkcja;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isForeign() {
        return foreign;
    }

    public void setForeign(boolean foreign) {
        this.foreign = foreign;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getTitleDegree() {
        return titleDegree;
    }

    public void setTitleDegree(String titleDegree) {
        this.titleDegree = titleDegree;
    }

    public WorkerFunction getFunkcja() {
        return funkcja;
    }

    public void setFunkcja(WorkerFunction funkcja) {
        this.funkcja = funkcja;
    }
}
