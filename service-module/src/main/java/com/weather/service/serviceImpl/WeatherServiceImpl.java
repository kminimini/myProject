package com.weather.service.serviceImpl;

import com.weather.common.model.WeatherData;
import com.weather.common.utils.ApiUtils;
import com.weather.service.repository.WeatherDataRepository;
import com.weather.service.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherDataRepository repository;

    @Autowired
    private ApiUtils apiUtils;

    @Autowired
    private RestTemplate restTemplate;

}
