package pl.poznan.put.data_import.model.worker;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "pracownicy")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkersWrapper {

    @XmlElement(name = "pracownik")
    private List<Worker> workers;

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers){
        this.workers = workers;
    }
}
