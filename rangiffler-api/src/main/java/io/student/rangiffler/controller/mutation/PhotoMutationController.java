package io.student.rangiffler.controller.mutation;

import io.student.rangiffler.model.Photo;
import io.student.rangiffler.model.PhotoInput;
import io.student.rangiffler.service.PhotoService;
import io.student.rangiffler.service.api.LikeService;
import io.student.rangiffler.service.api.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@PreAuthorize("isAuthenticated()")
public class PhotoMutationController {

    private final PhotoService photoService;
    private final LikeService likeService;
    private final UserService userService;

    public PhotoMutationController(PhotoService photoService,
                                   LikeService likeService,
                                   UserService userService) {
        this.photoService = photoService;
        this.likeService = likeService;
        this.userService = userService;
    }

    @MutationMapping
    public Photo photo(@AuthenticationPrincipal Jwt principal,
                       @Argument PhotoInput input) {
        String username = principal.getClaimAsString("sub");
        if (input.getId() != null) {
            return photoService.updatePhoto(username, input);
        } else {
            return photoService.createPhoto(username, input);
        }
    }

    @MutationMapping
    public void deletePhoto(@AuthenticationPrincipal Jwt principal, @Argument UUID id) {
        String username = principal.getClaimAsString("sub");
        photoService.deletePhoto(username, id);
    }

    @MutationMapping
    public Photo addPhotoLike(@AuthenticationPrincipal Jwt principal,
                              @Argument UUID photoId) {
        String username = principal.getClaimAsString("sub");
        UUID userId = userService.currentUser(username).getId();

        likeService.addLike(photoId, userId);
        return photoService.getPhotoById(photoId, username);
    }

    @MutationMapping
    public Photo removePhotoLike(@AuthenticationPrincipal Jwt principal,
                                 @Argument UUID photoId) {
        String username = principal.getClaimAsString("sub");
        UUID userId = userService.currentUser(username).getId();

        likeService.removeLike(photoId, userId);
        return photoService.getPhotoById(photoId, username);
    }

}
