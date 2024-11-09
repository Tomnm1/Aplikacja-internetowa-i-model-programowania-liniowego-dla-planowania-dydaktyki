package pl.poznan.put.data_import;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.poznan.put.data_import.insert_to_db.BuildingHandler;
import pl.poznan.put.data_import.insert_to_db.TeacherHandler;
import pl.poznan.put.data_import.model.buildings.Building;
import pl.poznan.put.data_import.model.plan.Plan;
import pl.poznan.put.data_import.model.worker.Worker;
import pl.poznan.put.data_import.util.XmlParser;

import java.io.File;
import java.util.List;

@Service
public class XmlReaderService {

    private final XmlParser xmlParser;
    private final BuildingHandler buildingHandler;
    private final TeacherHandler teacherHandler;

    @Autowired
    public XmlReaderService(
            XmlParser xmlParser,
            BuildingHandler buildingHandler,
            TeacherHandler teacherHandler) {
        this.xmlParser = xmlParser;
        this.buildingHandler = buildingHandler;
        this.teacherHandler = teacherHandler;
    }

    public Plan getPlanFromXml(File file) {
        return xmlParser.parseXmlFile(file);
    }

    public void insertDataToDB(Plan plan){
        List<Building> buildings = plan.getBuildings().getBuildings();
        buildingHandler.insertBuildings(buildings);
        List<Worker> workers = plan.getWorkers().getWorkers();
        teacherHandler.insertTeachers(workers);

    }
}
