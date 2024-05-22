package com.weather.common.utils;

import com.weather.common.model.ExcelData;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class ExcelUtils {

    private static final Logger logger = Logger.getLogger(ExcelUtils.class.getName());
    private static final int BATCH_SIZE = 500;

    public static List<ExcelData> extractExcelData(Sheet sheet) {
        List<ExcelData> excelDataList = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();

        if (!rowIterator.hasNext()) {
            logger.severe("Excel 파일에 행이 없음.");
            return excelDataList;
        }
        rowIterator.next(); // Skop header row

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            ExcelData excelData = new ExcelData();

            excelData.setDate(getCellValueAsString(row.getCell(0)));
            excelData.setTemperature(getCellValueAsDouble(row.getCell(1)));
            excelData.setFirstLevel(getCellValueAsString(row.getCell(2)));
            excelData.setSecondLevel(getCellValueAsString(row.getCell(3)));
            excelData.setThirdLevel(getCellValueAsString(row.getCell(4)));
            excelData.setGridX(getCellValueAsString(row.getCell(5)));
            excelData.setGridY(getCellValueAsString(row.getCell(6)));
            excelData.setLongitudeHour(getCellValueAsInt(row.getCell(7)));
            excelData.setLongitudeMinute(getCellValueAsInt(row.getCell(8)));
            excelData.setLongitudeSecond(getCellValueAsDouble(row.getCell(9)));
            excelData.setLatitudeHour(getCellValueAsInt(row.getCell(10)));
            excelData.setLatitudeMinute(getCellValueAsInt(row.getCell(11)));
            excelData.setLatitudeSecond(getCellValueAsDouble(row.getCell(12)));
            excelData.setLongitudeSecondPer100(getCellValueAsDouble(row.getCell(13)));
            excelData.setLatitudeSecondPer100(getCellValueAsDouble(row.getCell(14)));
            excelData.setLocationUpdate(getCellValueAsInt(row.getCell(15)));

            logger.info("데이터 추출 결과 >> " + excelData.toString());
            excelDataList.add(excelData);
        }
        return excelDataList;
    }

    /* TODO 셍의 유형애 관계없이 셀 값을 문자열로 변환
     *       'Cannot get a STRING value from a NUMERIC cell' 오류 방지
     *  TODO 셀이 null 인제 확인하고 null 인 경우 기본값을 반환. */
    public static String getCellValueAsString(Cell cell) {
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

    /* TODO 셀의 유형애 관계없이 셀 값을 문자열로 변환
     *       'Cannot get a STRING value from a NUMERIC cell' 오류 방지 */
    // TODO yield -> Java12부터 추가된 switch expression 의 일부, switch 블록 내에서 값을 반환할 때 사용.
    public static double getCellValueAsDouble(Cell cell) {
        if (cell == null) {
            return 0.0;
        }
        return switch (cell.getCellType()) {
            case STRING -> {
                try {
                    yield Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    logger.warning("문자열에서 이중 구문 분석에 실패.: " + cell.getStringCellValue());
                    yield 0.0;
                }
            }
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue() ? 1.0 : 0.0;
            case FORMULA -> cell.getNumericCellValue();
            default -> 0.0;
        };
    }

    /* TODO 셍의 유형애 관계없이 셀 값을 문자열로 변환
     *       'Cannot get a STRING value from a NUMERIC cell' 오류 방지 */
    // TODO yield -> Java12부터 추가된 switch expression 의 일부, switch 블록 내에서 값을 반환할 때 사용.
    public static int getCellValueAsInt(Cell cell) {
        if (cell == null) {
            return 0;
        }
        return switch (cell.getCellType()) {
            case STRING -> {
                try {
                    yield Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    logger.warning("문자열에서 이중 구문 분석에 실패.: " + cell.getStringCellValue());
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
