package com.pogoda.weather.interfaces;

import org.springframework.data.repository.CrudRepository;
import com.pogoda.weather.model.EspMeasureUnits;
import java.util.Optional;

public interface IEspMeasureUnitsRepository extends CrudRepository<EspMeasureUnits, String> {

    Optional<EspMeasureUnits> findByUnitId(String unitId);

    Optional<EspMeasureUnits> findByName(String name);

    boolean existsByUnitId(String unitId);

    boolean existsByName(String name);
}
