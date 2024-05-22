package com.weather.service.repository;

import com.weather.common.model.ExcelData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/*
    TODO Spring Data JPA 에서 커스텀 쿼리를 작성해서 파일 데이터 제한 횟수 풀기 -> ExcelDataRepositoryImpl
 */

public interface ExcelDataRepositoryCustom {
    // TODO ExcelData -> 리스트로 반환
    List<ExcelData> findAllExcelData();

    // TODO 검색결과 페이지 처리
    Page<ExcelData> findByLocationContaining(String query, Pageable pageable);
}
