package pl.poznan.put.xml_reader.model.plan;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.poznan.put.xml_reader.model.buildings.BuildingsWrapper;
import pl.poznan.put.xml_reader.model.worker.WorkersWrapper;

@XmlRootElement(name = "plan")
@XmlAccessorType(XmlAccessType.FIELD)
public class Plan {
    @XmlElement(name = "pracownicy")
    private WorkersWrapper workers;

    @XmlElement(name = "budynki")
    private BuildingsWrapper buildings;

    public WorkersWrapper getWorkers() {
        return workers;
    }

    public void setWorkers(WorkersWrapper workers) {
        this.workers = workers;
    }

    public BuildingsWrapper getBuildings() {
        return buildings;
    }

    public void setBuildings(BuildingsWrapper buildings) {
        this.buildings = buildings;
    }
}
