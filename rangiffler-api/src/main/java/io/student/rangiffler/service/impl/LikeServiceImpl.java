package io.student.rangiffler.service.impl;

import io.student.rangiffler.data.entity.LikeEntity;
import io.student.rangiffler.data.entity.PhotoLikeEntity;
import io.student.rangiffler.data.repository.LikeRepository;
import io.student.rangiffler.data.repository.PhotoLikeRepository;
import io.student.rangiffler.data.repository.PhotoRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.exception.ResourceNotFoundException;
import io.student.rangiffler.model.Like;
import io.student.rangiffler.model.Likes;
import io.student.rangiffler.service.api.LikeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {

    private final PhotoLikeRepository photoLikeRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PhotoRepository photoRepository;

    public LikeServiceImpl(PhotoLikeRepository photoLikeRepository,
                           UserRepository userRepository,
                           LikeRepository likeRepository,
                           PhotoRepository photoRepository) {
        this.photoLikeRepository = photoLikeRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.photoRepository = photoRepository;
    }


    @Override
    @Transactional
    public Like addLike(UUID photoId, UUID userId) {
        var photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Фото с id %s не найдено".formatted(photoId)));

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id %s не найден".formatted(userId)));

        var existingLike = photoLikeRepository.findByPhotoIdAndUserId(photoId, userId);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("Пользователь уже лайкал данное фото".formatted(userId));
        }

        var like = new LikeEntity()
                .setUser(user)
                .setCreatedDate(LocalDate.now());
        like = likeRepository.save(like);

        var photoLike = new PhotoLikeEntity()
                .setPhoto(photo)
                .setLike(like);
        photoLikeRepository.save(photoLike);

        return convertEntityToLike(like);
    }

    @Override
    @Transactional
    public void removeLike(UUID photoId, UUID userId) {
        var photoLike = photoLikeRepository.findByPhotoIdAndUserId(photoId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Лайк не найден"));

        photoLikeRepository.delete(photoLike);
    }

    @Override
    public Likes getPhotoLikes(UUID photoId) {
        var photoLikes = photoLikeRepository.findByPhotoId(photoId);

        var likes = photoLikes.stream()
                .map(pl -> convertEntityToLike(pl.getLike()))
                .collect(Collectors.toList());

        return new Likes()
                .setTotal(likes.size())
                .setLikes(likes);
    }

    private Like convertEntityToLike(LikeEntity entity) {
        return new Like()
                .setUser(entity.getUser().getId().toString())
                .setUsername(entity.getUser().getUsername())
                .setCreationDate(entity.getCreatedDate());
    }
}
