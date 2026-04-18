package io.student.rangiffler.controller.query;

import io.student.rangiffler.data.entity.PhotoEntity;
import io.student.rangiffler.data.repository.PhotoRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.exception.ResourceNotFoundException;
import io.student.rangiffler.model.*;
import io.student.rangiffler.service.impl.LikeServiceImpl;
import io.student.rangiffler.service.impl.UserServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class FeedQueryController {

    public static final String DATA_IMAGE_JPEG_BASE_64 = "data:image/jpeg;base64,";
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final LikeServiceImpl likeService;

    public FeedQueryController(PhotoRepository photoRepository,
                               UserRepository userRepository,
                               UserServiceImpl userService, LikeServiceImpl likeService) {
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.likeService = likeService;
    }

    @SchemaMapping(typeName = "Feed", field = "stat")
    public List<Stat> stat(Feed feed) {
        return userService.stat(feed.getUsername(), feed.getWithFriends());
    }

    @SchemaMapping(typeName = "Photo", field = "likes")
    public Likes likes(Photo photo) {
        return likeService.getPhotoLikes(photo.getId());
    }

    @SchemaMapping(typeName = "User", field = "photos")
    public Slice<Photo> photos(User user,
                               @Argument int page,
                               @Argument int size) {
        var pageable = PageRequest.of(page, size);
        var photoEntities = photoRepository.findByUserIdOrderByCreatedDateDesc(user.getId(), pageable);

        List<Photo> photos = photoEntities.stream()
                .map(entity -> convertToPhoto(entity, user.getId()))
                .collect(Collectors.toList());

        return new SliceImpl<>(photos, pageable, photoEntities.hasNext());
    }

    @SchemaMapping(typeName = "Feed", field = "photos")
    public Slice<Photo> photos(Feed feed,
                               @Argument int page,
                               @Argument int size) {
        var pageable = PageRequest.of(page, size);
        var user = userRepository.findByUsername(feed.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Пользователь %s не найден".formatted(feed.getUsername())
                ));

        var photoEntities = feed.getWithFriends()
                ? photoRepository.findByUserIdAndFriendsOrderByCreatedDateDesc(user.getId(), pageable)
                : photoRepository.findByUserIdOrderByCreatedDateDesc(user.getId(), pageable);

        var photos = photoEntities.getContent().stream()
                .map(entity -> convertToPhoto(entity, user.getId()))
                .toList();

        return new SliceImpl<>(photos, pageable, photoEntities.hasNext());
    }

    @QueryMapping
    public Feed feed(@AuthenticationPrincipal Jwt principal,
                     @Argument boolean withFriends) {
        var username = principal.getClaimAsString("sub");
        return Feed.newBuilder()
                .username(username)
                .withFriends(withFriends)
                .build();
    }

    private Photo convertToPhoto(PhotoEntity entity, UUID userId) {
        var countryFlag = DATA_IMAGE_JPEG_BASE_64 +
                Base64.getEncoder().encodeToString(entity.getCountry().getFlag());

        return new Photo()
                .setId(entity.getId())
                .setSrc(DATA_IMAGE_JPEG_BASE_64 + Base64.getEncoder().encodeToString(entity.getPhoto()))
                .setCountry(new Country()
                        .setCode(entity.getCountry().getCode())
                        .setName(entity.getCountry().getName())
                        .setFlag(countryFlag))
                .setDescription(entity.getDescription())
                .setCreationDate(entity.getCreatedDate().toLocalDate())
                .setOwner(true)
                .setLikes(new Likes());
    }
}
