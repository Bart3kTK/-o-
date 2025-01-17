package com.pogoda.weather.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.RequestParam;

import com.pogoda.weather.dto.EspUserDTO;

import com.pogoda.weather.dto.EspUserSettingsDTO;
import com.pogoda.weather.model.EspMeasureUnits;
import com.pogoda.weather.model.EspLanguages;
import com.pogoda.weather.model.EspUserSettings;
import com.pogoda.weather.model.EspUsers;
import com.pogoda.weather.repository.EspMeasureUnitsRepo;
import com.pogoda.weather.repository.EspLanguagesRepo;
import com.pogoda.weather.repository.EspUserSettingsRepo;
import com.pogoda.weather.repository.EspUsersRepo;

@Service
public class UserService {
    private final EspUsersRepo espUsersRepo;
    private final EspUserSettingsRepo espUsersSettingsRepo;
    private final EspLanguagesRepo languagesRepo;
    private final EspMeasureUnitsRepo measureUnitsRepo;

    public UserService(EspUsersRepo espUsersRepo, EspUserSettingsRepo espUsersSettingsRepo,
            EspLanguagesRepo languagesRepo, EspMeasureUnitsRepo measureUnitsRepo) {
        this.espUsersRepo = espUsersRepo;
        this.espUsersSettingsRepo = espUsersSettingsRepo;
        this.languagesRepo = languagesRepo;
        this.measureUnitsRepo = measureUnitsRepo;
    }

    public EspUserSettingsDTO getUserSettings(String login) {
        EspUsers user = espUsersRepo.getUserByLogin(login);
        EspUserSettings userSettings = espUsersSettingsRepo.getUserSettings(user.getId());

        if (userSettings != null) {
            return new EspUserSettingsDTO("metric", "english", false);
            }

            EspLanguages language = languagesRepo.getLanguageByLangId(userSettings.getLngId());
            EspMeasureUnits measureUnit = measureUnitsRepo.getUnitByUnitId(userSettings.getPreferedUnitId());
            
            return new EspUserSettingsDTO(measureUnit.getName(), language.getName(), userSettings.isDarkModeOn());
        }

    public EspUserSettingsDTO setUserSettings(EspUserSettingsDTO userSettingsDTO, String login) {
        EspUsers user = espUsersRepo.getUserByLogin(login);
        EspUserSettings userSettings = espUsersSettingsRepo.getUserSettings(user.getId());

        if (userSettings == null) {
            EspLanguages language = languagesRepo.getLanguageByName(userSettingsDTO.getLanguage());
            EspMeasureUnits measureUnit = measureUnitsRepo.getUnitByName(userSettingsDTO.getUnit());
            EspUserSettings newUserSettings = new EspUserSettings(user.getId(), language.getLangId(),
                    measureUnit.getUnitId(), userSettingsDTO.isDarkModeOn());
            espUsersSettingsRepo.saveUserSettings(newUserSettings);
            return userSettingsDTO;
        }

        EspLanguages language = languagesRepo.getLanguageByName(userSettingsDTO.getLanguage());
        EspMeasureUnits measureUnit = measureUnitsRepo.getUnitByName(userSettingsDTO.getUnit());
        userSettings.setLngId(language.getLangId());
        userSettings.setPreferedUnitId(measureUnit.getUnitId());
        userSettings.setDarkModeOn(userSettingsDTO.isDarkModeOn());
        espUsersSettingsRepo.saveUserSettings(userSettings);
        return userSettingsDTO;
    }

    // public EspUserSettingsDTO getUserSettings(String login) {
    // {
    // EspUsers user = espUsersRepo.getUserByLogin(login);
    // EspUserSettings userSettings = espUsersSettingsRepo.getUserSettings(user.getId());

    // if (userSettings != null) {
    // // return new EspUserSettingsDTO(darkModeOn);TODO
    // }

    // return null;
    // }
    // }

    public boolean passwordCheck(String login, String passwordHash) {
        if (!userExists(login)) {
            return false;
        }

        EspUsers userFromDb = espUsersRepo.getUserByLogin(login);
        return userFromDb.getPasswordHash().equals(passwordHash);
    }

    public boolean userExists(String login) {
        return espUsersRepo.userExists(login);
    }

    public EspUserDTO addUser(String login, String passwordHash) {
        if (userExists(login)) {
            return null;
        }
        EspUsers user = new EspUsers(login, passwordHash);
        EspUsers savedUser = espUsersRepo.saveUser(user);
        return new EspUserDTO(savedUser.getLogin(), "¯\\_(ツ)_/¯");
    }

    public EspUserDTO deleteUser(String login) {
        if (!userExists(login)) {
            return null;
        }
        espUsersRepo.deleteUserByLogin(login);
        return new EspUserDTO(login, "¯\\_(ツ)_/¯");
    }

    public EspUserDTO changePassword(String login, String newPasswordHash) {
        if (!userExists(login)) {
            return null;
        }
        EspUsers user = espUsersRepo.getUserByLogin(login);
        user.setPasswordHash(newPasswordHash);
        EspUsers savedUser = espUsersRepo.saveUser(user);
        return new EspUserDTO(savedUser.getLogin(), "¯\\_(ツ)_/¯");
    }

}
