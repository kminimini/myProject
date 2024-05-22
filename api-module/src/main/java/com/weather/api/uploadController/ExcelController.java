package com.weather.api.uploadController;

import com.weather.common.model.ExcelData;
import com.weather.common.utils.ExcelUtils;
import com.weather.service.service.ExcelService;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    private static final Logger logger = Logger.getLogger(ExcelController.class.getName());

    @Autowired
    private ExcelService excelService;

    // TODO 파일 DB에 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        logger.info("파일 수신: " + file.getOriginalFilename());

        if (file.isEmpty()) {
            logger.severe("비어있는 파일...");
            return ResponseEntity.badRequest().body("비어있는 파일...");
        }

        try {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            List<ExcelData> excelDataList = ExcelUtils.extractExcelData(sheet);
            if (excelDataList.isEmpty()) {
                workbook.close();
                return ResponseEntity.badRequest().body("Excel 파일에 유효한 데이터가 없음");
            }

            excelService.saveExcelData(excelDataList);
            workbook.close();
            return ResponseEntity.ok("업로드 성공!");
        } catch (IOException e) {
            logger.severe("파일 읽기 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body("Excel 파일 업로드 중 오류 발생.");
        } catch (Exception e) {
            logger.severe("업로드 데이터 형식을 다시 확인하시오.: " + e.getMessage());
            return ResponseEntity.badRequest().body("예기치 않은 오류가 발생했습니다.");
        }
    }

    // TODO ExcelData -> 리스트로 반환
    @GetMapping("/data")
    public ResponseEntity<List<ExcelData>> getAllData() {
        List<ExcelData> data = excelService.getAllExcelData();
        return ResponseEntity.ok(data);
    }

    // TODO 사용자가 선택한 지역에 해당하는 데이터를 검색하는 엔드포인트
    @GetMapping("/search")
    public ResponseEntity<Page<ExcelData>> searchExcelData(@RequestParam("query") String query,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstLevel").ascending());
        Page<ExcelData> data = excelService.searchDataByLocationPaged(query, pageable);
        return ResponseEntity.ok(data);
    }
}
