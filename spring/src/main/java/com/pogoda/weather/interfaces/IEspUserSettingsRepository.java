package com.pogoda.weather.interfaces;

import org.springframework.data.repository.CrudRepository;
import com.pogoda.weather.model.EspUserSettings;
import java.util.Optional;

public interface IEspUserSettingsRepository extends CrudRepository<EspUserSettings, String> {

    Optional<EspUserSettings> findByUserId(String userId);

    Iterable<EspUserSettings> findByLngId(int lngId);

    Iterable<EspUserSettings> findByDarkModeOn(boolean darkModeOn);
}
