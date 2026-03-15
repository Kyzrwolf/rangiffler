package io.student.rangiffler.data.repository;

import io.student.rangiffler.data.entity.PhotoLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhotoLikeRepository extends JpaRepository<PhotoLikeEntity, UUID> {

    Optional<PhotoLikeEntity> findByPhotoIdAndLikeId(UUID photoId, UUID likeId);

    List<PhotoLikeEntity> findByPhotoId(UUID photoId);

    @Query("SELECT pl FROM PhotoLikeEntity pl WHERE pl.photo.id = :photoId AND pl.like.user.id = :userId")
    Optional<PhotoLikeEntity> findByPhotoIdAndUserId(@Param("photoId") UUID photoId, @Param("userId") UUID userId);

    void deleteByPhotoId(UUID photoId);

}
