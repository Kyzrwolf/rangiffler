package io.student.rangiffler.service.api;

import io.student.rangiffler.model.Like;
import io.student.rangiffler.model.Likes;

import java.util.UUID;

public interface LikeService {
    Like addLike(UUID photoId, UUID userId);

    void removeLike(UUID photoId, UUID userId);

    Likes getPhotoLikes(UUID photoId);
}
