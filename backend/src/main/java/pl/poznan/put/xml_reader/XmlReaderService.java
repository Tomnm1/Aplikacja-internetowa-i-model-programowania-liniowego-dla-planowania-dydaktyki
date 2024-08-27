package pl.poznan.put.xml_reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.xml_reader.model.plan.Plan;
import pl.poznan.put.xml_reader.util.XmlParser;

import java.io.File;

@Service
public class XmlReaderService {

    private final XmlParser xmlParser;

    @Autowired
    public XmlReaderService(XmlParser xmlParser) {
        this.xmlParser = xmlParser;
    }

    public Plan getPlanFromXml(File file) {
        return xmlParser.parseXmlFile(file);
    }
}
