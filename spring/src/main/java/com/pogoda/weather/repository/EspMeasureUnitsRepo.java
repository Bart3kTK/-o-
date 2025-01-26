package com.pogoda.weather.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.pogoda.weather.interfaces.IEspMeasureUnitsRepository;
import com.pogoda.weather.model.EspMeasureUnits;

@Repository
public class EspMeasureUnitsRepo {

    private final IEspMeasureUnitsRepository espMeasureUnitsRepository;

    public EspMeasureUnitsRepo(IEspMeasureUnitsRepository espMeasureUnitsRepository) {
        this.espMeasureUnitsRepository = espMeasureUnitsRepository;
    }

    public EspMeasureUnits saveUnit(EspMeasureUnits espMeasureUnits) {
        return espMeasureUnitsRepository.save(espMeasureUnits);
    }

    public Optional<EspMeasureUnits> getUnitByUnitId(String unitId) {
        return espMeasureUnitsRepository.findByUnitId(unitId);
    }

    public Optional<EspMeasureUnits> getUnitByName(String name) {
        return espMeasureUnitsRepository.findByName(name);
    }

    public boolean unitExistsByUnitId(String unitId) {
        return espMeasureUnitsRepository.existsByUnitId(unitId);
    }

    public boolean unitExistsByName(String name) {
        return espMeasureUnitsRepository.existsByName(name);
    }

    public void deleteUnitByUnitId(String unitId) {
        espMeasureUnitsRepository.findByUnitId(unitId).ifPresent(espMeasureUnitsRepository::delete);
    }

    public void deleteUnitByName(String name) {
        espMeasureUnitsRepository.findByName(name).ifPresent(espMeasureUnitsRepository::delete);
    }

    public Iterable<EspMeasureUnits> getAllMeasureUnits() {
        return espMeasureUnitsRepository.findAll();
    }
}
