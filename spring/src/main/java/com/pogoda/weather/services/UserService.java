package com.pogoda.weather.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pogoda.weather.dto.EspUserSettingsDTO;
import com.pogoda.weather.model.EspUserSettings;
import com.pogoda.weather.model.EspUsers;
import com.pogoda.weather.repository.EspUserSettingsRepo;
import com.pogoda.weather.repository.EspUsersRepo;

@Service
public class UserService {
    private final EspUsersRepo espUsersRepo;
    private final EspUserSettingsRepo espUsersSettingsRepo;

    public UserService(EspUsersRepo espUsersRepo, EspUserSettingsRepo espUsersSettingsRepo) {
        this.espUsersRepo = espUsersRepo;
        this.espUsersSettingsRepo = espUsersSettingsRepo;
    }

    public EspUserSettingsDTO getUserSettings(String login) {
        {
            EspUsers user = espUsersRepo.getUserByLogin(login);
            EspUserSettings userSettings = espUsersSettingsRepo.getUserSettings(user.getId());

            if (userSettings != null) {
                // return new EspUserSettingsDTO(darkModeOn);TODO
            }

            return null;
        }
    }
}
