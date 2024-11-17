package com.pogoda.weather.repository;

import org.springframework.stereotype.Repository;
import com.pogoda.weather.interfaces.IEspUsersRepository;
import com.pogoda.weather.model.EspUsers;

@Repository
public class EspUsersRepo {

    private final IEspUsersRepository espUsersRepository;

    public EspUsersRepo(IEspUsersRepository espUsersRepository) {
        this.espUsersRepository = espUsersRepository;
    }

    public EspUsers saveUser(EspUsers espUsers) {
        return espUsersRepository.save(espUsers);
    }

    public EspUsers getUserByLogin(String login) {
        return espUsersRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found with login: " + login));
    }

    public boolean userExists(String login) {
        return espUsersRepository.existsByLogin(login);
    }

    public void deleteUserByLogin(String login) {
        espUsersRepository.findByLogin(login).ifPresent(espUsersRepository::delete);
    }
}
