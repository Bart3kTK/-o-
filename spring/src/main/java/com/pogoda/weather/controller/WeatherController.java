package com.pogoda.weather.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pogoda.weather.dto.EspAlertsDTO;
import com.pogoda.weather.dto.WeatherDTO;
import com.pogoda.weather.model.EspMeasurements;
import com.pogoda.weather.repository.EspAlertsRepo;
import com.pogoda.weather.repository.EspMeasurementsRepo;
import com.pogoda.weather.services.AlertService;
import com.pogoda.weather.services.UserService;
import com.pogoda.weather.services.WeatherService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private final WeatherService weatherService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final AlertService alertService;

    @Autowired
    private EspMeasurementsRepo espDataRepo;

    @PostMapping("/measurments")
    public ResponseEntity<EspMeasurements> zapis(@RequestBody EspMeasurements espMeasurements) {
        System.out.println("Dostalem dane " + espMeasurements.toString());
        return ResponseEntity.ok(espDataRepo.saveMeasurements(espMeasurements));
    }

    @GetMapping("/measurments/{id}")
    public ResponseEntity<EspMeasurements> odczyty(@PathVariable String id) {
        return ResponseEntity.ok(espDataRepo.getMeasurements(id));
    }

    @GetMapping("/measurments")
    public ResponseEntity<WeatherDTO> getWeatherData() {
        WeatherDTO weatherData = weatherService.getLatestWeather();
        if (weatherData != null) {
            return ResponseEntity.ok(weatherData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/alerts")
    public List<EspAlertsDTO> getAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/test")
    public String aja() {
        return "cos tam";
    }

    @PostMapping("/test")
    public void ajaaPOST() {
        System.out.println("DOSTALEM");
    }
}
