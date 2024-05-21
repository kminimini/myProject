package com.weather.service.serviceImpl;

import com.weather.common.model.ExcelData;
import com.weather.service.repository.ExcelDataRepository;
import com.weather.service.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelDataRepository excelDataRepository;

    // TODO 업로드 파일 DB에 저장
    @Override
    public void saveExcelData(List<ExcelData> excelDataList) {
        excelDataRepository.saveAll(excelDataList);
    }

    // TODO 데이터 저장 제한 해제 사용
    @Override
    public List<ExcelData> getAllExcelData() {
        return excelDataRepository.findAllExcelData();
    }

    // TODO DB에 저장된 파일 데이터 조회
    @Override
    public List<ExcelData> searchDataByLocation(String query) {
        return excelDataRepository.searchByLocation(query)
                .stream()
                .map(data -> {
                    // Id, Date, Temperature 열을 제외한 데이터만 유지
                    ExcelData filteredData = new ExcelData();
                    filteredData.setFirstLevel(data.getFirstLevel());
                    filteredData.setSecondLevel(data.getSecondLevel());
                    filteredData.setThirdLevel(data.getThirdLevel());
                    filteredData.setGridX(data.getGridX());
                    filteredData.setGridY(data.getGridY());
                    filteredData.setLongitudeHour(data.getLongitudeHour());
                    filteredData.setLongitudeMinute(data.getLongitudeMinute());
                    filteredData.setLongitudeSecond(data.getLongitudeSecond());
                    filteredData.setLatitudeHour(data.getLatitudeHour());
                    filteredData.setLatitudeMinute(data.getLatitudeMinute());
                    filteredData.setLatitudeSecond(data.getLatitudeSecond());
                    filteredData.setLongitudeSecondPer100(data.getLongitudeSecondPer100());
                    filteredData.setLatitudeSecondPer100(data.getLatitudeSecondPer100());
                    filteredData.setLocationUpdate(data.getLocationUpdate());

                    return filteredData;
                })
                .collect(Collectors.toList());
    }

    // TODO 페이지별로 검색 결과 반환
    @Override
    public Page<ExcelData> searchDataByLocationPaged(String query, Pageable pageable) {
        return excelDataRepository.searchByLocationPaged(query, pageable)
                .map(data -> {
                    ExcelData filteredData = new ExcelData();
                    filteredData.setFirstLevel(data.getFirstLevel());
                    filteredData.setSecondLevel(data.getSecondLevel());
                    filteredData.setThirdLevel(data.getThirdLevel());
                    filteredData.setGridX(data.getGridX());
                    filteredData.setGridY(data.getGridY());
                    filteredData.setLongitudeHour(data.getLongitudeHour());
                    filteredData.setLongitudeMinute(data.getLongitudeMinute());
                    filteredData.setLongitudeSecond(data.getLongitudeSecond());
                    filteredData.setLatitudeHour(data.getLatitudeHour());
                    filteredData.setLatitudeMinute(data.getLatitudeMinute());
                    filteredData.setLatitudeSecond(data.getLatitudeSecond());
                    filteredData.setLongitudeSecondPer100(data.getLongitudeSecondPer100());
                    filteredData.setLatitudeSecondPer100(data.getLatitudeSecondPer100());
                    filteredData.setLocationUpdate(data.getLocationUpdate());

                    return filteredData;
                });
    }
}
