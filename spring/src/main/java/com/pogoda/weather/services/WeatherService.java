package com.pogoda.weather.services;

import com.pogoda.weather.dto.EspAlertsDTO;
import com.pogoda.weather.dto.WeatherDTO;
import com.pogoda.weather.model.EspMeasurements;
import com.pogoda.weather.repository.EspMeasurementsRepo;

import java.util.ArrayList;
import java.util.List;

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

    public Iterable<WeatherDTO> getWeatherHistory() {
        System.out.println("getWeatherHistory");
        Iterable<EspMeasurements> measurements = espMeasurementsRepo.findAll();
        List<WeatherDTO> weatherDto = new ArrayList<>();

        measurements.forEach(measure -> {
            WeatherDTO weather = new WeatherDTO(measure.getPressure(), measure.getTemperature1(),
                    measure.getTemperature2(), measure.isRainDetected(), measure.getHumidity(),
                    measure.getLightIntensity(), measure.getGasConcentration());
            weatherDto.add(weather);
        });

        return weatherDto;
    }

    public Iterable<WeatherDTO> getWeatherByDate(int year, int month, int day) {
        Iterable<EspMeasurements> measurements = espMeasurementsRepo.findAll();
        List<WeatherDTO> weatherDto = new ArrayList<>();

        measurements.forEach(measure -> {
            if (measure.getDate().getYear() == year && measure.getDate().getMonthValue() == month
                    && measure.getDate().getDayOfMonth() == day) {
                WeatherDTO weather = new WeatherDTO(measure.getPressure(), measure.getTemperature1(),
                        measure.getTemperature2(), measure.isRainDetected(), measure.getHumidity(),
                        measure.getLightIntensity(), measure.getGasConcentration());
                weatherDto.add(weather);
            }
        });

        return weatherDto;
    }

    public WeatherDTO getAverangeMeasurmentsFromDay(int year, int month, int day) {
        Iterable<EspMeasurements> measurements = espMeasurementsRepo.findAll();
        int count = 0;
        int pressure = 0;
        int temperature1 = 0;
        int temperature2 = 0;
        boolean rainDetected = false;
        int humidity = 0;
        int lightIntensity = 0;
        int gasConcentration = 0;

        for (EspMeasurements measure : measurements) {
            if (measure.getDate().getYear() == year && measure.getDate().getMonthValue() == month
                    && measure.getDate().getDayOfMonth() == day) {
                pressure += measure.getPressure();
                temperature1 += measure.getTemperature1();
                temperature2 += measure.getTemperature2();
                if (measure.isRainDetected()) {
                    rainDetected = true;
                }
                humidity += measure.getHumidity();
                lightIntensity += measure.getLightIntensity();
                gasConcentration += measure.getGasConcentration();
                count++;
            }
        }
        ;

        if (count == 0) {
            return null;
        }

        return new WeatherDTO(pressure / count, temperature1 / count, temperature2 / count, rainDetected,
                humidity / count, lightIntensity / count, gasConcentration / count);
    }

}