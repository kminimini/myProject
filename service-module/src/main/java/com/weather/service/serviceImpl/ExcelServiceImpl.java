package com.weather.service.serviceImpl;

import com.weather.common.model.ExcelData;
import com.weather.service.repository.ExcelDataRepository;
import com.weather.service.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelDataRepository excelDataRepository;

    // TODO 업로드 파일 DB에 저장
    @Override
    public void saveExcelData(List<ExcelData> excelDataList) {
        excelDataRepository.saveAll(excelDataList);
    }

    // TODO ExcelData -> 리스트로 반환
    @Override
    public List<ExcelData> getAllExcelData() {
        return excelDataRepository.findAllExcelData();
    }

    // TODO 페이지별로 검색 결과 반환
    @Override
    public Page<ExcelData> searchDataByLocationPaged(String query, Pageable pageable) {
        return excelDataRepository.findByLocationContaining(query, pageable);
    }
}
