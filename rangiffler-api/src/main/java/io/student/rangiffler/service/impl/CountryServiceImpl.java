package io.student.rangiffler.service.impl;

import io.student.rangiffler.model.Country;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.service.api.CountryService;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country byCode(String code) {
        var entity = countryRepository.findByCode(code);
        if (entity == null) {
            throw new IllegalArgumentException("Не найдена страна по коду: " + code);
        }

        return new Country()
                .setCode(entity.getCode())
                .setName(entity.getName())
                .setFlag(toDataUriPng(entity.getFlag()));
    }

    private static String toDataUriPng(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(entity -> new Country()
                        .setCode(entity.getCode())
                        .setName(entity.getName())
                        .setFlag(toDataUriPng(entity.getFlag())))
                .toList();
    }
}
