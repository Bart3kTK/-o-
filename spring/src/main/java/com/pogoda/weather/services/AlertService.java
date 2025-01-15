package com.pogoda.weather.services;

import java.util.ArrayList;

import java.util.List;
import org.springframework.stereotype.Service;

import com.pogoda.weather.dto.EspAlertsDTO;
import com.pogoda.weather.model.EspAlerts;
import com.pogoda.weather.repository.EspAlertsRepo;
import com.pogoda.weather.repository.EspUsersAlertRepo;

@Service
public class AlertService {
    private final EspAlertsRepo espAlertsRepo;

    public AlertService(EspAlertsRepo espAlertsRepo) {
        this.espAlertsRepo = espAlertsRepo;
    }

    public List<EspAlertsDTO> getAllAlerts() {
        Iterable<EspAlerts> alerts = espAlertsRepo.getAllAlerts();

        List<EspAlertsDTO> alertsDTO = new ArrayList<>();

        alerts.forEach(alert -> {
            EspAlertsDTO alertDTO = new EspAlertsDTO();
            alertDTO.setAlertType(alert.getAlertType());
            alertsDTO.add(alertDTO);
        });

        return alertsDTO;
    }

}
