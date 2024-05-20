package com.weather.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.common.model.WeatherData;

import java.io.IOException;

public class ApiUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static WeatherData parseWeatherDataFromResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode body = root.path("body");
            if (!body.isMissingNode()) {
                JsonNode items = body.path("items");
                if (!items.isMissingNode() && items.isArray() && items.size() > 0) {
                    JsonNode item = items.get(0); // Assuming only one item is returned
                    WeatherData weatherData = new WeatherData();
                    weatherData.setBaseDate(item.path("baseDate").asText());
                    weatherData.setBaseTime(item.path("baseTime").asText());
                    weatherData.setFcstDate(item.path("fcstDate").asText());
                    weatherData.setFcstTime(item.path("fcstTime").asText());
                    weatherData.setCategory(item.path("category").asText());
                    weatherData.setFcstValue(item.path("fcstValue").asText());
                    weatherData.setNx(Integer.parseInt(item.path("nx").asText()));
                    weatherData.setNy(Integer.parseInt(item.path("ny").asText()));
                    return weatherData;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
