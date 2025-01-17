package com.pogoda.weather.services;

import java.util.ArrayList;

import java.util.List;
import org.springframework.stereotype.Service;

import com.pogoda.weather.dto.EspAlertsDTO;
import com.pogoda.weather.dto.EspLanguagesDTO;
import com.pogoda.weather.model.EspLanguages;
import com.pogoda.weather.repository.EspLanguagesRepo;

@Service
public class LanguagesService {

    private final EspLanguagesRepo espLanguagesRepo;

    public LanguagesService(EspLanguagesRepo espLanguagesRepo) {
        this.espLanguagesRepo = espLanguagesRepo;
    }

    public List<EspLanguagesDTO> getAllLanguages() {
        Iterable<EspLanguages> espLanguages = espLanguagesRepo.getAllLanguages();

        List<EspLanguagesDTO> languagesDTO = new ArrayList<>();

        espLanguages.forEach(language -> {
            EspLanguagesDTO languageDTO = new EspLanguagesDTO();
            languageDTO.setName(language.getName());
            languagesDTO.add(languageDTO);
        });

        return languagesDTO;

    }

    public EspLanguages addLanguage(EspLanguagesDTO espLanguagesDTO) {
        EspLanguages espLanguages = new EspLanguages();
        espLanguages.setName(espLanguagesDTO.getName());
        return espLanguagesRepo.saveLanguage(espLanguages);
    }
}