package com.pogoda.weather.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EspMeasurements {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private float pressure;
    private int temperature1;
    private int temperature2;
    private boolean rainDetected;
    private float humidity;
    private float lightIntensity;
    private float gasConcentration;

    private LocalDateTime date;

    public EspMeasurements(float pressure, int temperature1, int temperature2, boolean rainDetected, float humidity,
            float lightIntensity, float gasConcentration) {
        this.id = UUID.randomUUID().toString();
        this.pressure = pressure;
        this.temperature1 = temperature1;
        this.temperature2 = temperature2;
        this.rainDetected = rainDetected;
        this.humidity = humidity;
        this.lightIntensity = lightIntensity;
        this.gasConcentration = gasConcentration;
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Pressure: " + pressure + " temperature1 " + temperature1 + " temperature2 " + temperature2;
    }
}
