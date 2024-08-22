package pl.poznan.put.xml_reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.xml_reader.model.Worker;
import pl.poznan.put.xml_reader.model.WorkersWrapper;
import pl.poznan.put.xml_reader.util.XmlParser;

import java.io.File;
import java.util.List;

@Service
public class XmlReaderService {

    private final XmlParser xmlParser;

    @Autowired
    public XmlReaderService(XmlParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    public List<Worker> getWorkersFromXml(File file) {
        WorkersWrapper wrapper = xmlParser.parseXmlFile(file);
        return wrapper.getPracownicy();
    }
}
