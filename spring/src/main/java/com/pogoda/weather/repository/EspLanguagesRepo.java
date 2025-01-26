package com.pogoda.weather.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import com.pogoda.weather.interfaces.IEspLanguagesRepository;
import com.pogoda.weather.model.EspLanguages;

@Repository
public class EspLanguagesRepo {

    private final IEspLanguagesRepository espLanguagesRepository;

    public EspLanguagesRepo(IEspLanguagesRepository espLanguagesRepository) {
        this.espLanguagesRepository = espLanguagesRepository;
    }

    public EspLanguages saveLanguage(EspLanguages espLanguages) {
        return espLanguagesRepository.save(espLanguages);
    }

    public Optional<EspLanguages> getLanguageByLangId(String langId) {
        return espLanguagesRepository.findByLangId(langId);
    }

    public Optional<EspLanguages> getLanguageByName(String name) {
        return espLanguagesRepository.findByName(name);
    }

    public Iterable<EspLanguages> getAllLanguages() {
        return espLanguagesRepository.findAll();
    }

    public boolean languageExistsByLangId(String langId) {
        return espLanguagesRepository.existsByLangId(langId);
    }

    public boolean languageExistsByName(String name) {
        return espLanguagesRepository.existsByName(name);
    }

    public void deleteLanguageByLangId(String langId) {
        espLanguagesRepository.findByLangId(langId).ifPresent(espLanguagesRepository::delete);
    }

    public void deleteLanguageByName(String name) {
        espLanguagesRepository.findByName(name).ifPresent(espLanguagesRepository::delete);
    }
}
