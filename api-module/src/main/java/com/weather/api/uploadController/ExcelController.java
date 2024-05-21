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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    private static final Logger logger = Logger.getLogger(ExcelController.class.getName());

    @Autowired
    private ExcelService excelService;

    private static final int BATCH_SIZE = 500;

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
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext()) {
                logger.severe("Excel 파일에 행이 없음");
                workbook.close();
                return ResponseEntity.badRequest().body("Excel 파일에 행이 없음");
            }
            rowIterator.next(); // Skip header row

            List<ExcelData> excelDataList = new ArrayList<>();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                ExcelData excelData = new ExcelData();

                // Extract and set the values from each cell
                excelData.setDate(ExcelUtils.getCellValueAsString(row.getCell(0)));
                excelData.setTemperature(ExcelUtils.getCellValueAsDouble(row.getCell(1)));
                excelData.setFirstLevel(ExcelUtils.getCellValueAsString(row.getCell(2)));
                excelData.setSecondLevel(ExcelUtils.getCellValueAsString(row.getCell(3)));
                excelData.setThirdLevel(ExcelUtils.getCellValueAsString(row.getCell(4)));
                excelData.setGridX(ExcelUtils.getCellValueAsString(row.getCell(5)));
                excelData.setGridY(ExcelUtils.getCellValueAsString(row.getCell(6)));
                excelData.setLongitudeHour(ExcelUtils.getCellValueAsInt(row.getCell(7)));
                excelData.setLongitudeMinute(ExcelUtils.getCellValueAsInt(row.getCell(8)));
                excelData.setLongitudeSecond(ExcelUtils.getCellValueAsDouble(row.getCell(9)));
                excelData.setLatitudeHour(ExcelUtils.getCellValueAsInt(row.getCell(10)));
                excelData.setLatitudeMinute(ExcelUtils.getCellValueAsInt(row.getCell(11)));
                excelData.setLatitudeSecond(ExcelUtils.getCellValueAsDouble(row.getCell(12)));
                excelData.setLongitudeSecondPer100(ExcelUtils.getCellValueAsDouble(row.getCell(13)));
                excelData.setLatitudeSecondPer100(ExcelUtils.getCellValueAsDouble(row.getCell(14)));
                excelData.setLocationUpdate(ExcelUtils.getCellValueAsInt(row.getCell(15)));

                logger.info("데이터 추출 결과 >> " + excelData.toString());

                excelDataList.add(excelData);

                if (excelDataList.size() >= BATCH_SIZE) {
                    excelService.saveExcelData(excelDataList);
                    excelDataList.clear();
                }
            }

            if (!excelDataList.isEmpty()) {
                excelService.saveExcelData(excelDataList);
            }

            workbook.close();
            return ResponseEntity.ok("업로드 성공!");
        } catch (IOException e) {
            logger.severe("파일 읽기 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body("Excel 파일 업로드 중 오류 발생.");
        } catch (Exception e) {
            logger.severe("업로드 데이터 형식을 다시 확인하시오.: " + e.getMessage());
            return ResponseEntity.badRequest().body("예기치 않은 오류가 발생했습니다..");
        }
    }

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

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    // TODO yield -> Java12부터 추가된 switch expression 의 일부, switch 블록 내에서 값을 반환할 때 사용.
    private double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        return switch (cell.getCellType()) {
            case STRING -> {
                try {
                    yield Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    logger.warning("Failed to parse double from string: " + cell.getStringCellValue());
                    yield 0.0;
                }
            }
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue() ? 1.0 : 0.0;
            case FORMULA -> cell.getNumericCellValue();
            default -> 0.0;
        };
    }

    // TODO yield -> Java12부터 추가된 switch expression 의 일부, switch 블록 내에서 값을 반환할 때 사용.
    private int getCellValueAsInt(Cell cell) {
        if (cell == null) {
            return 0;
        }
        return switch (cell.getCellType()) {
            case STRING -> {
                try {
                    yield Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    logger.warning("Failed to parse int from string: " + cell.getStringCellValue());
                    yield 0;
                }
            }
            case NUMERIC -> (int) cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue() ? 1 : 0;
            case FORMULA -> (int) cell.getNumericCellValue();
            default -> 0;
        };
    }
}
