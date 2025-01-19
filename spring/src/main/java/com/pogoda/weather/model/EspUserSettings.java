package com.pogoda.weather.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class EspUserSettings {

    @Id
    private String userId;
    private String preferedUnitId;
    private String lngId;
    private boolean darkModeOn;

    public EspUserSettings(String userId, String preferedUnitId, String lngId, boolean darkModeOn) {
        this.userId = userId;
        this.preferedUnitId = preferedUnitId;
        this.lngId = lngId;
        this.darkModeOn = darkModeOn;
    }

}