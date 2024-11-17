package com.pogoda.weather.interfaces;

import org.springframework.data.repository.CrudRepository;
import com.pogoda.weather.model.EspUsersAlert;

import java.util.Optional;

public interface IEspUsersAlertRepository extends CrudRepository<EspUsersAlert, String> {

    // Znajdź alerty powiązane z użytkownikiem na podstawie userId
    Iterable<EspUsersAlert> findByUserId(String userId);

    // Znajdź alert na podstawie userId i alertId
    Optional<EspUsersAlert> findByUserIdAndAlertId(String userId, String alertId);

    // Sprawdź, czy użytkownik ma powiązany alert
    boolean existsByUserIdAndAlertId(String userId, String alertId);

    // Usuń alert na podstawie userId i alertId
    void deleteByUserIdAndAlertId(String userId, String alertId);
}
