package com.pogoda.weather.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pogoda.weather.dto.EspAlertsDTO;
import com.pogoda.weather.dto.EspLanguagesDTO;
import com.pogoda.weather.dto.EspMeasureUnitsDTO;
import com.pogoda.weather.dto.WeatherDTO;
import com.pogoda.weather.model.EspLanguages;
import com.pogoda.weather.model.EspMeasureUnits;
import com.pogoda.weather.dto.EspUserDTO;
import com.pogoda.weather.model.EspAlerts;
import com.pogoda.weather.model.EspMeasurements;
import com.pogoda.weather.repository.EspMeasurementsRepo;
import com.pogoda.weather.services.AlertService;
import com.pogoda.weather.services.LanguagesService;
import com.pogoda.weather.services.MeasurementsUnitsService;
import com.pogoda.weather.services.UserService;
import com.pogoda.weather.services.WeatherService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private final WeatherService weatherService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final AlertService alertService;

    @Autowired
    private final LanguagesService languagesService;

    @Autowired
    private final MeasurementsUnitsService measurementsUnitsService;

    @Autowired
    private EspMeasurementsRepo espDataRepo;

    @PostMapping("/measurments")
    public ResponseEntity<EspMeasurements> zapis(@RequestBody EspMeasurements espMeasurements) {
        System.out.println("Dostalem dane " + espMeasurements.toString());
        return ResponseEntity.ok(espDataRepo.saveMeasurements(espMeasurements));
    }

    @GetMapping("/measurments/{id}")
    public ResponseEntity<EspMeasurements> odczyty(@PathVariable String id) {
        return ResponseEntity.ok(espDataRepo.getMeasurements(id));
    }

    @GetMapping("/measurments")
    public ResponseEntity<WeatherDTO> getWeatherData() {
        WeatherDTO weatherData = weatherService.getLatestWeather();
        if (weatherData != null) {
            return ResponseEntity.ok(weatherData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/alerts")
    public Iterable<EspAlertsDTO> getAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/languages")
    public List<EspLanguagesDTO> getLanguages() {
        return languagesService.getAllLanguages();
    }

    @PostMapping("/languages")
    public ResponseEntity<EspLanguages> saveLanguage(@RequestBody EspLanguagesDTO espLanguagesDTO) {
        return ResponseEntity.ok(languagesService.addLanguage(espLanguagesDTO));
    }

    @GetMapping("/measureunits")
    public List<EspMeasureUnitsDTO> getMeasureUnits() {
        return measurementsUnitsService.getAllMeasureUnits();
    }

    @PostMapping("/measureunits")
    public ResponseEntity<EspMeasureUnits> saveMeasureUnit(@RequestBody EspMeasureUnitsDTO espMeasureUnitsDTO) {
        return ResponseEntity.ok(measurementsUnitsService.addMeasureUnit(espMeasureUnitsDTO));
    }

    @GetMapping("/test")
    public String aja() {
        return "cos tam";
    }

    // Tak wyglada dodawanie alertu
    // POST http://localhost:8080/weather/alerts
    // {
    // "alertType": "Banana"
    // }
    @PostMapping("/alerts")
    public ResponseEntity<EspAlerts> addAlert(@RequestBody EspAlertsDTO alert) {
        System.out.println("Dostalem alert " + alert.toString());
        return ResponseEntity.ok(alertService.addAlert(alert));
    }

    // Login jest unikatowy aktualnie!!!
    // false gdy haslo sie nie zgadza albo login nie istnieje
    // GET http://localhost:8080/weather/users/password_check?login=Banana&password=123
    @GetMapping("/users/password_check")
    public boolean passwordCheck(@RequestParam String login, @RequestParam String password) {
        return userService.passwordCheck(login, password);
    }

    // Tak wyglada login check
    // GET http://localhost:8080/weather/users/login_check?login=Banana
    @GetMapping("/users/login_check")
    public boolean loginCheck(@RequestParam String login) {
        return userService.userExists(login);
    }

    // Tak wyglada dodawanie usera
    // POST http://localhost:8080/weather/users/add
    // {
    // "login": "Banana",
    // "password": "123"
    // }
    @PostMapping("/users/add")
    public ResponseEntity<EspUserDTO> postMethodName(@RequestBody EspUserDTO user) {
        System.out.println("Dostalem usera " + user.toString());
        if (userService.userExists(user.getLogin())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.addUser(user.getLogin(), user.getPassword()));
    }

    // Tak wyglada usuwanie usera
    // DELETE http://localhost:8080/weather/users/delete?login=Banana
    @GetMapping("/users/delete")
    public ResponseEntity<EspUserDTO> deleteUser(@RequestParam String login) {
        if (!userService.userExists(login)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.deleteUser(login));
    }

    // Tak wyglada zmiana hasla
    // PUT http://localhost:8080/weather/users/change_password
    // {
    // "login": "Banana",
    // "password": "123"
    // }
    @PostMapping("/users/change_password")
    public ResponseEntity<EspUserDTO> changePassword(@RequestBody EspUserDTO user) {
        if (!userService.userExists(user.getLogin())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.changePassword(user.getLogin(), user.getPassword()));
    }

    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody EspUserDTO user) {
        if (!userService.userExists(user.getLogin())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login does not exist");
        }
        boolean passwordValid = userService.passwordCheck(user.getLogin(), user.getPassword());
        if (!passwordValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        String token = userService.generateToken(user.getLogin());
        return ResponseEntity.ok(token);
    }

}
