package com.weather.service.serviceImpl;

import com.weather.common.constants.ApiConstants;
import com.weather.common.model.WeatherData;
import com.weather.common.utils.ApiUtils;
import com.weather.service.repository.WeatherDataRepository;
import com.weather.service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean saveWeatherData(String nx, String ny, String baseDate, String baseTime) {
        String url = ApiConstants.OPEN_API_URL +
                "?serviceKey=" + ApiConstants.SERVICE_KEY +
                "&numOfRows=" + ApiConstants.DEFAULT_NUM_OF_ROWS +
                "&pageNo=" + ApiConstants.DEFAULT_PAGE_NO +
                "&dataType=" + ApiConstants.DEFAULT_DATA_TYPE +
                "&base_date=" + baseDate +
                "&base_time=" + baseTime +
                "&nx=" + nx +
                "&ny=" + ny;

        String response = restTemplate.getForObject(url, String.class);
        WeatherData weatherData = ApiUtils.parseWeatherDataFromResponse(response);

        if (weatherData != null) {
            weatherDataRepository.save(weatherData);
            return true;
        }

        return false;
    }

    @Override
    public String getWeatherForecast(int nx, int ny, String baseDate, String baseTime) {
        // OpenAPI 호출을 위한 URL 구성
        String url = apiUrl + "?serviceKey=" + apiKey
                + "&numOfRows=10"
                + "&pageNo=1"
                + "&dataType=JSON"
                + "&base_date=" + baseDate
                + "&base_time=" + baseTime
                + "&nx=" + nx
                + "&ny=" + ny;

        // OpenAPI 호출
        String response = restTemplate.getForObject(url, String.class);

        // 콘솔에 결과 출력
        System.out.println("Weather forecast response: " + response);

        return response;
    }

}
