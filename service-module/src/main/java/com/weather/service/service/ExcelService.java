package com.weather.service.service;

import com.weather.common.model.ExcelData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExcelService {
    // TODO 업로드 파일 DB에 저장
    void saveExcelData(List<ExcelData> excelDataList);

    // TODO 데이터 저장 제한 해제 사용
    List<ExcelData> getAllExcelData();

    // TODO 페이지별로 검색 결과 반환
    Page<ExcelData> searchDataByLocationPaged(String query, Pageable pageable);

    // TODO API 호출과 데이터 저장
    void saveForecastDataToDatabase(String baseDate, String baseTime, int nx, int ny, String serviceKey);
}
