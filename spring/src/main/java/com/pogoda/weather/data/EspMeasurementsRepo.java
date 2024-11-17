package com.pogoda.weather.data;

import org.springframework.stereotype.Repository;
import com.pogoda.weather.interfaces.IEspMeasurementsReposytory;
import com.pogoda.weather.model.EspMeasurements;


@Repository
public class EspMeasurementsRepo {

    private final IEspMeasurementsReposytory espMeasurementsReposytory;

    public EspMeasurementsRepo(IEspMeasurementsReposytory espMeasurementsReposytory) {
        this.espMeasurementsReposytory = espMeasurementsReposytory;
    }

    public EspMeasurements saveMeasurements(EspMeasurements espMeasurements) {
        return espMeasurementsReposytory.save(espMeasurements);
    }

    public EspMeasurements getMeasurements(String id) {
        return espMeasurementsReposytory.findById(id)
                .orElseThrow(() -> new RuntimeException("Measurement not found with id: " + id));
    }

    public Iterable<EspMeasurements> getMeasurementsByPressure(int pressure) {
        return espMeasurementsReposytory.findByPressure1(pressure);
    }

    public Iterable<EspMeasurements> getMeasurementsByTemperature(int temperature) {
        return espMeasurementsReposytory.findByTemperature1(temperature);
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

}
