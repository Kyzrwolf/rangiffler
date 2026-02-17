package io.student.rangiffler.service;

import io.student.rangiffler.data.PhotoEntity;
import io.student.rangiffler.model.Country;
import io.student.rangiffler.model.Likes;
import io.student.rangiffler.model.Photo;
import io.student.rangiffler.model.PhotoInput;
import io.student.rangiffler.repository.CountryRepository;
import io.student.rangiffler.repository.PhotoRepository;
import io.student.rangiffler.repository.UserRepository;
import io.student.rangiffler.utils.Utils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;


    public PhotoService(PhotoRepository photoRepository,
                        CountryRepository countryRepository,
                        UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
    }

    public Photo createPhoto(String username, PhotoInput photoInput) {
        UUID userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username))
                .getId();

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

        return new Photo()
                .setId(saved.getId())
                .setSrc(photoInput.getSrc())
                .setCountry(new Country()
                        .setCode(countryEntity.getCode())
                        .setName(countryEntity.getName())
                        .setFlag(countryFlag))
                .setDescription(saved.getDescription())
                .setCreationDate(saved.getCreatedDate().toLocalDate())
                .setLikes(new Likes())
                .setOwner(true);
    }

    public void deletePhoto(String username, UUID photoId) {
        UUID userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username))
                .getId();

        var entity = photoRepository.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("Фото не найдено: " + photoId));

        if (!userId.equals(entity.getUserId())) {
            throw new AccessDeniedException("Нельзя удалить чужое фото");
        }

        photoRepository.delete(entity);
    }
}
