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
public class EspLanguages {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String langId;
    private String name;

    public EspLanguages(String name) {
        this.name = name;
    }
}
