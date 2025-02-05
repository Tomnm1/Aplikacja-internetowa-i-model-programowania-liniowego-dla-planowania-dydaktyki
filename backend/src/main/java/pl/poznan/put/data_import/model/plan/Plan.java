package pl.poznan.put.data_import.model.plan;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import pl.poznan.put.data_import.model.buildings.BuildingsWrapper;
import pl.poznan.put.data_import.model.subject_groups.SubjectGroupsWrapper;
import pl.poznan.put.data_import.model.subjects.DidacticCycles;
import pl.poznan.put.data_import.model.worker.WorkersWrapper;

@XmlRootElement(name = "plan")
@XmlAccessorType(XmlAccessType.FIELD)
public class Plan {
    @XmlElement(name = "pracownicy")
    private WorkersWrapper workers;

    @XmlElement(name = "budynki")
    private BuildingsWrapper buildings;

    @XmlElement(name = "cykleDydaktyczne")
    private DidacticCycles didacticCycles;

    @XmlElement(name = "grupyPrzedmiotow")
    private SubjectGroupsWrapper subjectGroupWrapper;

    // Getter i Setter
    public SubjectGroupsWrapper getSubjectGroupWrapper() {
        return subjectGroupWrapper;
    }

    public void setSubjectGroupWrapper(SubjectGroupsWrapper subjectGroupWrapper) {
        this.subjectGroupWrapper = subjectGroupWrapper;
    }

    public DidacticCycles getDidacticCycles() {
        return didacticCycles;
    }

    public void setDidacticCycles(DidacticCycles didacticCycles) {
        this.didacticCycles = didacticCycles;
    }

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
