package com.pogoda.weather.services;

import com.pogoda.weather.dto.WeatherDTO;
import com.pogoda.weather.model.EspMeasurements;
import com.pogoda.weather.repository.EspMeasurementsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final EspMeasurementsRepo espMeasurementsRepo;

    @Autowired
    public WeatherService(EspMeasurementsRepo espMeasurementsRepo) {
        this.espMeasurementsRepo = espMeasurementsRepo;
    }

    public WeatherDTO getLatestWeather() {
        EspMeasurements latestMeasurement = espMeasurementsRepo.getLatestEspMeasurements();

        if (latestMeasurement != null) {
            return new WeatherDTO(latestMeasurement.getPressure(), latestMeasurement.getTemperature1(),
                    latestMeasurement.getTemperature2(), latestMeasurement.isRainDetected(),
                    latestMeasurement.getHumidity(), latestMeasurement.getLightIntensity(),
                    latestMeasurement.getGasConcentration());
        }
        return null;
    }
}
