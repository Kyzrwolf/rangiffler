package io.student.rangiffler.service;

import io.student.rangiffler.data.PhotoEntity;
import io.student.rangiffler.model.Country;
import io.student.rangiffler.model.Likes;
import io.student.rangiffler.model.Photo;
import io.student.rangiffler.model.PhotoInput;
import io.student.rangiffler.repository.CountryRepository;
import io.student.rangiffler.repository.PhotoRepository;
import io.student.rangiffler.utils.Utils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final CountryRepository countryRepository;


    public PhotoService(PhotoRepository photoRepository, CountryRepository countryRepository) {
        this.photoRepository = photoRepository;
        this.countryRepository = countryRepository;
    }

    public Photo createPhoto(UUID userId, PhotoInput photoInput) {
        var countryEntity = countryRepository.findByCode(
                photoInput.getCountry().getCode());

        if (countryEntity == null) {
            throw new IllegalArgumentException("Не найдена страна по коду: " + photoInput.getCountry().getCode());
        }

        var entity = new PhotoEntity();
        var decodedPhoto = Utils.decodeDataUriBase64(photoInput.getSrc());
        entity.setUserId(userId);
        entity.setCountry(countryEntity);
        entity.setDescription(photoInput.getDescription());
        entity.setPhoto(decodedPhoto);
        entity.setCreatedDate(LocalDateTime.now());

        PhotoEntity saved = photoRepository.save(entity);
        var countryFlag = "data:image/png;base64," + Base64.getEncoder().encodeToString(countryEntity.getFlag());

        return Photo.newBuilder()
                .id(saved.getId().toString())
                .src(photoInput.getSrc())
                .country(Country.newBuilder()
                        .code(countryEntity.getCode())
                        .name(countryEntity.getName())
                        .flag(countryFlag)
                        .build())
                .description(saved.getDescription())
                .creationDate(saved.getCreatedDate().toLocalDate())
                .likes(new Likes(0, Collections.emptyList()))
                .isOwner(true)
                .build();
    }
}
