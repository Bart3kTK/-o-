package com.pogoda.weather.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.pogoda.weather.dto.EspMeasureUnitsDTO;
import com.pogoda.weather.model.EspMeasureUnits;
import com.pogoda.weather.repository.EspMeasureUnitsRepo;

@Service
public class MeasurementsUnitsService {

    private final EspMeasureUnitsRepo espMeasureUnitsRepo;

    public MeasurementsUnitsService(EspMeasureUnitsRepo espMeasureUnitsRepo) {
        this.espMeasureUnitsRepo = espMeasureUnitsRepo;
    }

    public List<EspMeasureUnitsDTO> getAllMeasureUnits() {
        Iterable<EspMeasureUnits> espMeasureUnits = espMeasureUnitsRepo.getAllMeasureUnits();

        List<EspMeasureUnitsDTO> measureUnitsDTO = new ArrayList<>();

        espMeasureUnits.forEach(measureUnit -> {
            EspMeasureUnitsDTO measureUnitDTO = new EspMeasureUnitsDTO();
            measureUnitDTO.setName(measureUnit.getName());
            measureUnitsDTO.add(measureUnitDTO);
        });

        return measureUnitsDTO;
    }

    public EspMeasureUnits addMeasureUnit(EspMeasureUnitsDTO espMeasureUnitsDTO) {
        EspMeasureUnits espMeasureUnits = new EspMeasureUnits();
        espMeasureUnits.setName(espMeasureUnitsDTO.getName());
        return espMeasureUnitsRepo.saveUnit(espMeasureUnits);
    }
}
