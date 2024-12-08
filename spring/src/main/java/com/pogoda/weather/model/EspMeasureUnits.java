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

public class EspMeasureUnits {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String unitId;
    private String name;

    @OneToMany(mappedBy = "preferredUnit")
    private List<EspUserSettings> userSettings;

    public EspMeasureUnits(String name) {
        this.name = name;
    }
}
