package com.pogoda.weather.interfaces;

import org.springframework.data.repository.CrudRepository;
import com.pogoda.weather.model.EspAlerts;
import java.util.Optional;

public interface IEspAlertsRepository extends CrudRepository<EspAlerts, String> {

    Optional<EspAlerts> findByAlertId(String alertId);

    Optional<EspAlerts> findByAlertType(String alertType);

    boolean existsByAlertId(String alertId);

    boolean existsByAlertType(String alertType);
}
