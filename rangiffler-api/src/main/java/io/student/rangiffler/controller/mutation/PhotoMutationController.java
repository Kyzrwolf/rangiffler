package io.student.rangiffler.controller.mutation;

import io.student.rangiffler.model.Photo;
import io.student.rangiffler.model.PhotoInput;
import io.student.rangiffler.repository.PhotoRepository;
import io.student.rangiffler.repository.UserRepository;
import io.student.rangiffler.service.PhotoService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@PreAuthorize("isAuthenticated()")
public class PhotoMutationController {

    private final PhotoService photoService;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    public PhotoMutationController(PhotoService photoService,
                                   PhotoRepository photoRepository,
                                   UserRepository userRepository) {
        this.photoService = photoService;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    @MutationMapping
    public Photo photo(@AuthenticationPrincipal Jwt principal,
                       @Argument PhotoInput input) {

        String username = principal.getClaimAsString("sub");
        UUID userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username))
                .getId();

        return photoService.createPhoto(userId, input);
    }

    @MutationMapping
    public void deletePhoto(@AuthenticationPrincipal Jwt principal, @Argument UUID id) {
        String username = principal.getClaimAsString("sub");
        UUID userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username))
                .getId();

        var entity = photoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фото не найдено: " + id));


        if (!userId.equals(entity.getUserId())) {
            throw new AccessDeniedException("Нельзя удалить чужое фото");
        }

        photoRepository.delete(entity);
    }

}
