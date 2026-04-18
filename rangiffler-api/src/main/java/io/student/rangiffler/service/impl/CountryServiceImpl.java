package io.student.rangiffler.service.impl;

import io.student.rangiffler.data.entity.CountryEntity;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.model.Country;
import io.student.rangiffler.service.api.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    public static final String DATA_IMAGE_PNG_BASE_64 = "data:image/png;base64,";
    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Country> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::toCountryModel)
                .toList();
    }

    private Country toCountryModel(CountryEntity entity) {
        return new Country()
                .setCode(entity.getCode())
                .setName(entity.getName())
                .setFlag(toBase64DataUri(entity.getFlag()));
    }

    private String toBase64DataUri(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return "";
        return DATA_IMAGE_PNG_BASE_64 + Base64.getEncoder().encodeToString(bytes);
    }
}
