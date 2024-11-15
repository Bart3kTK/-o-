package com.pogoda.weather.interfaces;

import org.springframework.data.repository.CrudRepository;
import com.pogoda.weather.model.EspUsers;
import java.util.Optional;

public interface IEspUsersRepository extends CrudRepository<EspUsers, String> {

    Optional<EspUsers> findByLogin(String login);

    boolean existsByLogin(String login);
}