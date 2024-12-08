package com.pogoda.weather.model;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(EspUsersAlert.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EspUsersAlert implements Serializable {
    @Id
    private String userId;
    @Id
    private String alertId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private EspUsers user;

    @ManyToOne
    @JoinColumn(name = "alertId", nullable = false)
    private EspAlerts alert;


}
