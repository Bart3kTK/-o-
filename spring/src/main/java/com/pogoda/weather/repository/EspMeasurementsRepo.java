package com.pogoda.weather.repository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.pogoda.weather.dto.WeatherDTO;
import com.pogoda.weather.interfaces.IEspMeasurementsReposytory;
import com.pogoda.weather.model.EspMeasurements;

@Repository
public class EspMeasurementsRepo {

    private final IEspMeasurementsReposytory espMeasurementsReposytory;

    public EspMeasurementsRepo(IEspMeasurementsReposytory espMeasurementsReposytory) {
        this.espMeasurementsReposytory = espMeasurementsReposytory;
    }

    public EspMeasurements saveMeasurements(EspMeasurements espMeasurements) {
        espMeasurements.setDate(LocalDateTime.now());
        return espMeasurementsReposytory.save(espMeasurements);
    }

    public EspMeasurements getMeasurements(String id) {
        return espMeasurementsReposytory.findById(id)
                .orElseThrow(() -> new RuntimeException("Measurement not found with id: " + id));
    }

    public Iterable<EspMeasurements> getMeasurementsByPressure(int pressure) {
        return espMeasurementsReposytory.findByPressure(pressure);
    }

    public Iterable<EspMeasurements> getMeasurementsByTemperature(int temperature) {
        return espMeasurementsReposytory.findByTemperature1(temperature);
    }

    public EspMeasurements getLatestEspMeasurements() {
        return espMeasurementsReposytory.findTopByOrderByDateDesc()
                .orElseThrow(() -> new RuntimeException("No measurements found"));
    }

    public void deleteMeasurement(String id) {
        espMeasurementsReposytory.deleteById(id);
    }

    public void deleteMeasurement(EspMeasurements espMeasurements) {
        espMeasurementsReposytory.delete(espMeasurements);
    }

    public void deleteAllMeasurements() {
        espMeasurementsReposytory.deleteAll();
    }

    public WeatherDTO toDTO(EspMeasurements entity) {
        return new WeatherDTO(entity.getPressure(), entity.getTemperature1(), entity.getTemperature2(),
                entity.isRainDetected(), entity.getHumidity(), entity.getLightIntensity(),
                entity.getGasConcentration());
    }

    public EspMeasurements fromDTO(WeatherDTO dto) {
        return new EspMeasurements(dto.getPressure(), dto.getTemperature1(), dto.getTemperature2(),
                dto.isRainDetected(), dto.getHumidity(), dto.getLightIntensity(), dto.getGasConcentration());
    }

}
