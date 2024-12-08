package com.pogoda.weather.services;

import com.pogoda.weather.repository.EspMeasurementsRepo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@org.springframework.stereotype.Service
public class Service {
    EspMeasurementsRepo espDataRepo;

    public void tuBedzieLogika() {
        System.out.println("To przeciez logiczne");
    }

}
