package com.pogoda.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pogoda.weather.data.EspMeasurementsRepo;
import com.pogoda.weather.model.EspMeasurements;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/weather")
public class Controller {

    @Autowired
    EspMeasurementsRepo espDataRepo;

    @GetMapping("/test")
    public String aja() {
        return "cos tam";
    }

    @PostMapping("/test")
    public void ajaa() {
        System.out.println("DOSTALEM");
    }

    @PostMapping("/measurments")
    public ResponseEntity<EspMeasurements> zapis(@RequestBody EspMeasurements espMeasurements) {
        return ResponseEntity.ok(espDataRepo.saveMeasurements(espMeasurements));
    }

    @GetMapping("/measurments/{id}")
    public ResponseEntity<EspMeasurements> odczyty(@PathVariable String id) {
        return ResponseEntity.ok(espDataRepo.getMeasurements(id));
    }
}
