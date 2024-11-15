package com.pogoda.weather.data;

import org.springframework.stereotype.Repository;
import com.pogoda.weather.interfaces.IEspUsersAlertRepository;
import com.pogoda.weather.model.EspUsersAlert;

@Repository
public class EspUsersAlertRepo  {

    private final IEspUsersAlertRepository espUsersAlertRepository;

    public EspUsersAlertRepo(IEspUsersAlertRepository espUsersAlertRepository) {
        this.espUsersAlertRepository = espUsersAlertRepository;
    }

    public EspUsersAlert saveUserAlert(EspUsersAlert espUsersAlert) {
        return espUsersAlertRepository.save(espUsersAlert);
    }

    public EspUsersAlert getUserAlert(String userId, String alertId) {
        return espUsersAlertRepository.findByUserIdAndAlertId(userId, alertId).orElseThrow(
                () -> new RuntimeException("Alert not found for userId: " + userId + " and alertId: " + alertId));
    }

    public boolean userAlertExists(String userId, String alertId) {
        return espUsersAlertRepository.existsByUserIdAndAlertId(userId, alertId);
    }

    public void deleteUserAlert(String userId, String alertId) {
        espUsersAlertRepository.deleteByUserIdAndAlertId(userId, alertId);
    }

    public Iterable<EspUsersAlert> getUserAlerts(String userId) {
        return espUsersAlertRepository.findByUserId(userId);
    }
}
