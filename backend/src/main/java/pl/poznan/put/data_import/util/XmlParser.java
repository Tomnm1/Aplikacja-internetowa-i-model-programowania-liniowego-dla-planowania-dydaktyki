package pl.poznan.put.data_import.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Component;
import pl.poznan.put.data_import.model.plan.Plan;

import java.io.File;

@Component
public class XmlParser {
    public Plan parseXmlFile(File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Plan.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (Plan) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            throw new RuntimeException("Błąd podczas parsowania pliku XML", e);
        }
    }
}
