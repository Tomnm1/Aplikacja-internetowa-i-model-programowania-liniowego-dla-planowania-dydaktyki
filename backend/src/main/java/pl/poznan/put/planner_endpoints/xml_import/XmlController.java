package pl.poznan.put.planner_endpoints.xml_import;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.poznan.put.xml_reader.XmlReaderService;
import pl.poznan.put.xml_reader.model.plan.Plan;

import java.io.File;

@RestController
@RequestMapping("/api/xml")
public class XmlController {

    private final XmlReaderService xmlReaderService;

    @Autowired
    public XmlController(XmlReaderService xmlReaderService) {
        this.xmlReaderService = xmlReaderService;
    }

    @PostMapping("/importXml")
    public ResponseEntity<Plan> importXml(@RequestParam("file") MultipartFile file) {
        try {
            File xmlFile = File.createTempFile("temp", null);
            file.transferTo(xmlFile);
            Plan plan = xmlReaderService.getPlanFromXml(xmlFile);
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
