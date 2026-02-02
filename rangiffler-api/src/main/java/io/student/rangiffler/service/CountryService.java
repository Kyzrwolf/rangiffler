package io.student.rangiffler.service;

import io.student.rangiffler.model.Country;
import io.student.rangiffler.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> findAll() {
        return countryRepository.findAll()
                .stream()
                .map(entity -> Country.newBuilder()
                        .code(entity.getCode())
                        .name(entity.getName())
                        .flag(toDataUriPng(entity.getFlag()))
                        .build())
                .toList();
    }

    public Country byCode(String code) {
        var entity = countryRepository.findByCode(code);
        if (entity == null) {
            throw new IllegalArgumentException("Не найдена страна по коду: " + code);
        }

        return Country.newBuilder()
                .code(entity.getCode())
                .name(entity.getName())
                .flag(toDataUriPng(entity.getFlag()))
                .build();
    }

    private static String toDataUriPng(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
    }
}
