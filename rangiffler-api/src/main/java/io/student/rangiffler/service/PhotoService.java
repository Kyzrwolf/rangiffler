package io.student.rangiffler.service;

import io.student.rangiffler.data.entity.PhotoEntity;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.data.repository.PhotoRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.exception.ResourceNotFoundException;
import io.student.rangiffler.model.Country;
import io.student.rangiffler.model.Likes;
import io.student.rangiffler.model.Photo;
import io.student.rangiffler.model.PhotoInput;
import io.student.rangiffler.service.api.LikeService;
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
    private final LikeService likeService;

    public PhotoService(PhotoRepository photoRepository,
                        CountryRepository countryRepository,
                        UserRepository userRepository, LikeService likeService) {
        this.photoRepository = photoRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
        this.likeService = likeService;
    }

    public Photo createPhoto(String username, PhotoInput photoInput) {
        UUID userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username))
                .getId();

        var countryEntity = countryRepository.findByCode(
                photoInput.getCountry().getCode())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Страна не найдена по коду: %s", photoInput.getCountry().getCode())
                ));

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

    public Photo getPhotoById(UUID photoId, String currentUsername) {
        var photoEntity = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Фото не найдено: " + photoId));

        var userEntity = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден: " + currentUsername));

        boolean isOwner = photoEntity.getUserId().equals(userEntity.getId());

        var countryFlag = "data:image/png;base64," +
                Base64.getEncoder().encodeToString(photoEntity.getCountry().getFlag());

        return new Photo()
                .setId(photoEntity.getId())
                .setSrc("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photoEntity.getPhoto()))
                .setCountry(new Country()
                        .setCode(photoEntity.getCountry().getCode())
                        .setName(photoEntity.getCountry().getName())
                        .setFlag(countryFlag))
                .setDescription(photoEntity.getDescription())
                .setCreationDate(photoEntity.getCreatedDate().toLocalDate())
                .setOwner(isOwner)
                .setLikes(likeService.getPhotoLikes(photoId));
    }

    public Photo updatePhoto(String username, PhotoInput photoInput) {
        UUID userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username))
                .getId();

        var photoEntity = photoRepository.findById(photoInput.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Фото не найдено: " + photoInput.getId()));

        if (!userId.equals(photoEntity.getUserId())) {
            throw new AccessDeniedException("Нельзя редактировать чужое фото");
        }

        if (photoInput.getSrc() != null) {
            var decodedPhoto = Utils.decodeDataUriBase64(photoInput.getSrc());
            photoEntity.setPhoto(decodedPhoto);
        }

        if (photoInput.getCountry() != null) {
            var countryEntity = countryRepository.findByCode(photoInput.getCountry().getCode())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Страна не найдена по коду: %s", photoInput.getCountry().getCode())
                    ));
            photoEntity.setCountry(countryEntity);
        }

        if (photoInput.getDescription() != null) {
            photoEntity.setDescription(photoInput.getDescription());
        }

        PhotoEntity saved = photoRepository.save(photoEntity);
        var countryFlag = "data:image/png;base64," + Base64.getEncoder().encodeToString(saved.getCountry().getFlag());

        return new Photo()
                .setId(saved.getId())
                .setSrc("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(saved.getPhoto()))
                .setCountry(new Country()
                        .setCode(saved.getCountry().getCode())
                        .setName(saved.getCountry().getName())
                        .setFlag(countryFlag))
                .setDescription(saved.getDescription())
                .setCreationDate(saved.getCreatedDate().toLocalDate())
                .setLikes(likeService.getPhotoLikes(saved.getId()))
                .setOwner(true);
    }
}
