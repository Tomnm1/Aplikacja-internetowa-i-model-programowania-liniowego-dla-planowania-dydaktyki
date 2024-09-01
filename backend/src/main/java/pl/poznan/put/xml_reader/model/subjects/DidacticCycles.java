package pl.poznan.put.xml_reader.model.subjects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DidacticCycles {

    @XmlElement(name = "cyklDydaktyczny")
    private List<DidacticCycle> didacticCycles;

    public List<DidacticCycle> getDidacticCycles() {
        return didacticCycles;
    }

    public void setDidacticCycles(List<DidacticCycle> didacticCycles) {
        this.didacticCycles = didacticCycles;
    }
}
