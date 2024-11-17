package com.pogoda.weather.repository;

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

    public EspUserSettings getUserSettings(int userId) {
        return espUserSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User settings not found for userId: " + userId));
    }

    public void deleteUserSettings(int userId) {
        espUserSettingsRepository.findByUserId(userId).ifPresent(espUserSettingsRepository::delete);
    }

    public Iterable<EspUserSettings> getUserSettingsByLngId(int lngId) {
        return espUserSettingsRepository.findByLngId(lngId);
    }

    public Iterable<EspUserSettings> getUserSettingsByDarkModeOn(boolean darkModeOn) {
        return espUserSettingsRepository.findByDarkModeOn(darkModeOn);
    }
}
