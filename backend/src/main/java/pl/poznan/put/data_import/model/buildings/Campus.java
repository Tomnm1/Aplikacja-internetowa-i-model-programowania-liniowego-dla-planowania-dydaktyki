package pl.poznan.put.data_import.model.buildings;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "kampus")
@XmlAccessorType(XmlAccessType.FIELD)
public class Campus {
    @XmlAttribute(name = "ref")
    private int ref;

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }
}
