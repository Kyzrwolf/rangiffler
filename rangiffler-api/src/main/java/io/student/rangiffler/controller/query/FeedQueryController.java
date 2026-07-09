package io.student.rangiffler.controller.query;

import io.student.rangiffler.model.*;
import io.student.rangiffler.service.PhotoService;
import io.student.rangiffler.service.impl.LikeServiceImpl;
import io.student.rangiffler.service.impl.UserServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FeedQueryController {

    private final PhotoService photoService;
    private final UserServiceImpl userService;
    private final LikeServiceImpl likeService;

    public FeedQueryController(PhotoService photoService,
                               UserServiceImpl userService,
                               LikeServiceImpl likeService) {
        this.photoService = photoService;
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
        return photoService.getPhotosByUserId(user.getId(), PageRequest.of(page, size));
    }

    @SchemaMapping(typeName = "Feed", field = "photos")
    public Slice<Photo> photos(Feed feed,
                               @Argument int page,
                               @Argument int size) {
        return photoService.getPhotosByFeed(feed.getUsername(), feed.getWithFriends(), PageRequest.of(page, size));
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
}
