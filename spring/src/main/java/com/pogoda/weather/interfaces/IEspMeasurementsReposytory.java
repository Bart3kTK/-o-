package com.pogoda.weather.interfaces;

import org.springframework.data.repository.CrudRepository;
import com.pogoda.weather.model.EspMeasurements;

import java.time.LocalDate;
import java.util.Optional;

public interface IEspMeasurementsReposytory extends CrudRepository<EspMeasurements, String> {

    @SuppressWarnings("unchecked")
    EspMeasurements save(EspMeasurements espMeasurements);

    Iterable<EspMeasurements> findByPressure(int pressure);

    Iterable<EspMeasurements> findByTemperature1(int temperature);

    Optional<EspMeasurements> findById(String id);

    void deleteById(String id);

    void delete(EspMeasurements entity);

    void deleteAll();

    Iterable<EspMeasurements> findAll();

    Optional<EspMeasurements> findTopByOrderByDateDesc();
}
