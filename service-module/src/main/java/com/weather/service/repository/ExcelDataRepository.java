package com.weather.service.repository;

import com.weather.common.model.ExcelData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelDataRepository extends JpaRepository<ExcelData, Long>, ExcelDataRepositoryCustom {
}
