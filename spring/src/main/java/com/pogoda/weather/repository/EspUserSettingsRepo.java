package com.pogoda.weather.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.pogoda.weather.interfaces.IEspUserSettingsRepository;
import com.pogoda.weather.model.EspUserSettings;

@Repository
public class EspUserSettingsRepo {

    private final IEspUserSettingsRepository espUserSettingsRepository;

    public EspUserSettingsRepo(IEspUserSettingsRepository espUserSettingsRepository) {
        this.espUserSettingsRepository = espUserSettingsRepository;
    }

    public EspUserSettings saveUserSettings(EspUserSettings espUserSettings) {
        return espUserSettingsRepository.save(espUserSettings);
    }

    public Optional<EspUserSettings> getUserSettings(String userId) {
        return espUserSettingsRepository.findByUserId(userId);
    }

    public void deleteUserSettings(String userId) {
        espUserSettingsRepository.findByUserId(userId).ifPresent(espUserSettingsRepository::delete);
    }

    public Iterable<EspUserSettings> getUserSettingsByLngId(String lngId) {
        return espUserSettingsRepository.findByLngId(lngId);
    }

    public Iterable<EspUserSettings> getUserSettingsByDarkModeOn(boolean darkModeOn) {
        return espUserSettingsRepository.findByDarkModeOn(darkModeOn);
    }
}
