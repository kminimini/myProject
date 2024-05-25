package com.weather.service.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.common.constants.ExcelConstants;
import com.weather.common.model.ExcelData;
import com.weather.common.model.WeatherData;
import com.weather.service.repository.ExcelDataRepository;
import com.weather.service.repository.WeatherDataRepository;
import com.weather.service.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private ExcelDataRepository excelDataRepository;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

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

    // TODO API 호출과 데이터 저장
    @Override
    public void saveForecastDataToDatabase(String baseDate, String baseTime, int nx, int ny, String serviceKey) {
        try {
            // baseDate 요청 값의 형식을 변경하여 YYYYMMDD 형태로 만듭니다.
            String formattedBaseDate = formatBaseDate(baseDate);

            // API 호출을 위한 URL 생성
            String apiUrl = ExcelConstants.BASE_URL +
                    "?serviceKey=" + serviceKey +
                    "&numOfRows=10" +
                    "&pageNo=1" +
                    "&dataType=JSON" +
                    "&base_date=" + formattedBaseDate +
                    "&base_time=" + baseTime +
                    "&nx=" + nx +
                    "&ny=" + ny;

            String responseBody = new RestTemplate().getForObject(apiUrl, String.class);
            List<WeatherData> weatherDataList = parseResponse(responseBody);

            weatherDataRepository.saveAll(weatherDataList);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save forecast data to database.", e);
        }
    }

    // baseDate 요청 값을 YYYYMMDD 형태로 변경하는 메서드
    private String formatBaseDate(String baseDate) {
        LocalDate date = LocalDate.parse(baseDate);
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    // API 응답을 파싱하여 WeatherData 객체로 변환하는 메서드
    private List<WeatherData> parseResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<WeatherData> weatherDataList = objectMapper.readValue(responseBody, new TypeReference<List<WeatherData>>() {});
            return weatherDataList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse response body.", e);
        }
    }
}
