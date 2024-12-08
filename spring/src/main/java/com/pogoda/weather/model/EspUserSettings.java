package com.pogoda.weather.model;



import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@NoArgsConstructor
@Getter
@Setter

public class EspUserSettings {

    @Id
    private int userId;
    private int preferedUnitId;
    private int lngId;
    private boolean darkModeOn;

    @ManyToOne
    @JoinColumn(name = "lang_id", nullable = false)
    private EspLanguages language;

    @ManyToOne
    @JoinColumn(name = "preferred_unit_id", nullable = false)
    private EspMeasureUnits preferredUnit;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private EspUsers user;

    public EspUserSettings(int userId, int preferedUnitId, int lngId, boolean darkModeOn) {
        this.userId = userId;
        this.preferedUnitId = preferedUnitId;
        this.lngId = lngId;
        this.darkModeOn = darkModeOn;
    }

}