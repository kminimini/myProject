package com.weather.common.utils;

import org.apache.poi.ss.usermodel.Cell;

import java.util.logging.Logger;

public class ExcelUtils {

    private static final Logger logger = Logger.getLogger(ExcelUtils.class.getName());

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

    /* TODO 셍의 유형애 관계없이 셀 값을 문자열로 변환
     *       'Cannot get a STRING value from a NUMERIC cell' 오류 방지 */
    public static double getCellValueAsDouble(Cell cell) {
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

    /* TODO 셍의 유형애 관계없이 셀 값을 문자열로 변환
     *       'Cannot get a STRING value from a NUMERIC cell' 오류 방지 */
    public static int getCellValueAsInt(Cell cell) {
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
