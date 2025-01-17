package com.pogoda.weather.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.pogoda.weather.dto.EspUserDTO;
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
