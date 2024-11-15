package com.pogoda.weather.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EspMeasurements {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int pressure1;
    private int pressure2;
    private int temperature1;
    private int temperature2; // i tu bedzie wiecej teraz tylko model

    public EspMeasurements(int press1, int press2, int temp1, int temp2) {
        this.pressure1 = press1;
        this.pressure2 = press2;
        this.temperature1 = temp1;
        this.temperature2 = temp2;
    }
}
