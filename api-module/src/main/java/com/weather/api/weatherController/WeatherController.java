package com.weather.api.weatherController;

import com.weather.service.service.ExcelService;
import com.weather.service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private ExcelService excelService;

}
