package com.pogoda.weather.interfaces;

import org.springframework.data.repository.CrudRepository;
import com.pogoda.weather.model.EspLanguages;
import java.util.Optional;

public interface IEspLanguagesRepository extends CrudRepository<EspLanguages, String> {

    Optional<EspLanguages> findByLangId(String langId);

    Optional<EspLanguages> findByName(String name);

    boolean existsByLangId(String langId);

    boolean existsByName(String name);
}
