package com.weather.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiUtils {

    @Value("${weather.api.url}")
    private String baseUrl;

    @Value("${weather.api.serviceKey}")
    private String serviceKey;

}
