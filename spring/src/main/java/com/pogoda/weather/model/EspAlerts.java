package com.pogoda.weather.model;


import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class EspAlerts {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String alertId;
    private String alertType;

    @OneToMany(mappedBy = "alert")
    private List<EspUsersAlert> userAlerts;

    public EspAlerts(String alertType) {
        this.alertType = alertType;
    }

}