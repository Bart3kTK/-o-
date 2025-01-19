package com.pogoda.weather.services;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.RequestParam;

import com.pogoda.weather.configuration.JWTConfig;
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

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Service
@AllArgsConstructor
@Getter
@Setter
public class UserService {

    private final EspUsersRepo espUsersRepo;
    private final EspUserSettingsRepo espUsersSettingsRepo;
    private final Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(16, 32, 1, 19923, 2);
    private final EspLanguagesRepo languagesRepo;
    private final EspMeasureUnitsRepo measureUnitsRepo;

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

    public boolean passwordCheck(String login, String password) {
        if (!userExists(login)) {
            return false;
        }

        String passwordHash = espUsersRepo.getUserPassword(login);
        return passwordEncoder.matches(password, passwordHash);
    }

    public boolean userExists(String login) {
        return espUsersRepo.userExists(login);
    }

    public EspUserDTO addUser(String login, String password) {
        if (userExists(login)) {
            return null;
        }
        String passwordHash = passwordEncoder.encode(password);
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

    public String generateToken(String login) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        long expiration = 860000000;

        return Jwts.builder().setSubject(login).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(key).compact();
    }

}
