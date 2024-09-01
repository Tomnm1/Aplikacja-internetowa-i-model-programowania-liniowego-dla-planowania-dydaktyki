package pl.poznan.put.xml_reader.model.buildings;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "budynki")
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildingsWrapper {
    @XmlElement(name = "budynek")
    private List<Building> buildings;

    public List<Building> getBuildings(){
        return buildings;
    }

    public void setBuildings(List<Building> buildings){
        this.buildings = buildings;
    }
}
