package com.weather.common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ExcelData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date; // 구분
    private double temperature; // 행정구역코드
    private String firstLevel; // 1단계
    private String secondLevel; // 2단계
    private String thirdLevel; // 3단계
    private String gridX; // 격자 X
    private String gridY; // 격자 Y
    private int longitudeHour; // 경도(시)
    private int longitudeMinute; // 경도(분)
    private double longitudeSecond; // 경도(초)
    private int latitudeHour; // 위도(시)
    private int latitudeMinute; // 위도(분)
    private double latitudeSecond; // 위도(초)
    private double longitudeSecondPer100; // 경도(초/100)
    private double latitudeSecondPer100; // 위도(초/100)
    private int locationUpdate; // 위치업데이트
}
