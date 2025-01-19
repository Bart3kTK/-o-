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
import com.pogoda.weather.model.EspUserSettings;
import com.pogoda.weather.model.EspUsers;
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
    private final Argon2PasswordEncoder passwordEncoder;

    // public UserService(EspUsersRepo espUsersRepo, EspUserSettingsRepo espUsersSettingsRepo) {
    // this.espUsersRepo = espUsersRepo;
    // this.espUsersSettingsRepo = espUsersSettingsRepo;
    // }

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
