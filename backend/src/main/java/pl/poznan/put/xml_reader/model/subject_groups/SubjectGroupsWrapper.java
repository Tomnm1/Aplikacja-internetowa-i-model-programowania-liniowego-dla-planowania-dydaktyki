package pl.poznan.put.xml_reader.model.subject_groups;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectGroupsWrapper {

    @XmlElement(name = "grupaPrzedmiotow")
    private List<SubjectGroup> subjectGroups;

    public List<SubjectGroup> getSubjectGroups() {
        return subjectGroups;
    }

    public void setSubjectGroups(List<SubjectGroup> subjectGroups) {
        this.subjectGroups = subjectGroups;
    }
}
