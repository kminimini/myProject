package com.weather.service.service;

public interface WeatherService {
    boolean saveWeatherData(String nx, String ny, String baseDate, String baseTime);

    String getWeatherForecast(int nx, int ny, String baseDate, String baseTime);
}
