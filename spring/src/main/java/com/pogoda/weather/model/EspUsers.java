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
public class EspUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String login;
    private String passwordHash;

    @OneToMany(mappedBy = "userId")
    private List<EspUsersAlert> alerts;
    
    @OneToMany(mappedBy = "userId")
    private List<EspUserSettings> settings;

    public EspUsers(String login, String passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
    }

}
