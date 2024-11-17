package com.pogoda.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {
    private float pressure;
    private int temperature1;
    private int temperature2;
    private boolean rainDetected;
    private float humidity;
    private float lightIntensity;
    private float gasConcentration;
}
