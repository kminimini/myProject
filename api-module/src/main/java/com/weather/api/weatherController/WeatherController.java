package com.weather.api.weatherController;

import com.weather.service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /*  TODO 특정 위치의 날씨 데이터를 저장하는 엔드포인트 */
    @PostMapping("/save")
    public ResponseEntity<String> saveWeatherData(@RequestParam String nx, @RequestParam String ny,
                                                  @RequestParam String baseDate, @RequestParam String baseTime) {
        boolean isSuccess = weatherService.saveWeatherData(nx, ny, baseDate, baseTime);
        if (isSuccess) {
            return ResponseEntity.ok("Weather data saved successfully!");
        } else {
            return ResponseEntity.badRequest().body("Failed to save weather data.");
        }
    }

    /* TODO 특정 위치의 날씨 예보를 가져오는 엔드포인트 */
    @GetMapping("/forecast")
    public ResponseEntity<String> getWeatherForecast(@RequestParam int nx, @RequestParam int ny,
                                                     @RequestParam String baseDate, @RequestParam String baseTime) {
        String forecast = weatherService.getWeatherForecast(nx, ny, baseDate, baseTime);
        return ResponseEntity.ok(forecast);
    }
}
