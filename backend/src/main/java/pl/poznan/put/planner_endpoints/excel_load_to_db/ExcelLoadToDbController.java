package pl.poznan.put.planner_endpoints.excel_load_to_db;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.poznan.put.data_import.ExcelToDbService;

import java.io.InputStream;

@RestController
@RequestMapping("/api/excelToDb")
public class ExcelLoadToDbController {

    private final ExcelToDbService excelToDbService;
    @Autowired
    public ExcelLoadToDbController(
        ExcelToDbService excelToDbService
    ){
        this.excelToDbService = excelToDbService;
    }

    @PostMapping("/importLoadSheet")
    public ResponseEntity<String> importLoadSheet(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty!");
        }
        try(InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            excelToDbService.startSheetProcessing(sheet);
            return ResponseEntity.ok("File loaded");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Excel import error: " + e);
        }
    }
}
