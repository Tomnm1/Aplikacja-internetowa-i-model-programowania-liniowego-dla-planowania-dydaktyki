package pl.poznan.put.xml_reader.model;

import jakarta.xml.bind.annotation.*;

public class WorkerFunction {

    private int unitRef;

    private String roleTitle;

    @XmlElement(name = "jednostkaOrganizacyjna")
    public void setUnitOrganizational(UnitOrganizational unit) {
        this.unitRef = unit.getRef();
    }

    @XmlElement(name = "nazwa")
    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public int getUnitRef() {
        return unitRef;
    }

    public void setUnitRef(int unitRef) {
        this.unitRef = unitRef;
    }

    // inner class to handle ref
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class UnitOrganizational {
        @XmlAttribute(name = "ref")
        private int ref;

        public int getRef() {
            return ref;
        }

        public void setRef(int ref) {
            this.ref = ref;
        }
    }
}