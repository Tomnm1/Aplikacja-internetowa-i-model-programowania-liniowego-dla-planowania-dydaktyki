package pl.poznan.put.data_import.model.subject_groups;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectGroup {

    @XmlElement(name = "kod")
    private String code;

    @XmlElement(name = "nazwa")
    private String name;

    @XmlElement(name = "przedmiot")
    private List<SubjectReference> subjects;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubjectReference> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectReference> subjects) {
        this.subjects = subjects;
    }
}
