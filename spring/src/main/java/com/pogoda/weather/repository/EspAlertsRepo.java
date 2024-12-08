package com.pogoda.weather.repository;

import org.springframework.stereotype.Repository;
import com.pogoda.weather.interfaces.IEspAlertsRepository;
import com.pogoda.weather.model.EspAlerts;

@Repository
public class EspAlertsRepo {

    private final IEspAlertsRepository espAlertsRepository;

    public EspAlertsRepo(IEspAlertsRepository espAlertsRepository) {
        this.espAlertsRepository = espAlertsRepository;
    }

    public EspAlerts saveAlert(EspAlerts espAlerts) {
        return espAlertsRepository.save(espAlerts);
    }

    public EspAlerts getAlertByAlertId(String alertId) {
        return espAlertsRepository.findByAlertId(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found with alertId: " + alertId));
    }

    public EspAlerts getAlertByAlertType(String alertType) {
        return espAlertsRepository.findByAlertType(alertType)
                .orElseThrow(() -> new RuntimeException("Alert not found with alertType: " + alertType));
    }

    public boolean alertExistsByAlertId(String alertId) {
        return espAlertsRepository.existsByAlertId(alertId);
    }

    public boolean alertExistsByAlertType(String alertType) {
        return espAlertsRepository.existsByAlertType(alertType);
    }

    public void deleteAlertByAlertId(String alertId) {
        espAlertsRepository.findByAlertId(alertId).ifPresent(espAlertsRepository::delete);
    }

    public void deleteAlertByAlertType(String alertType) {
        espAlertsRepository.findByAlertType(alertType).ifPresent(espAlertsRepository::delete);
    }
}
