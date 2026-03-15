package io.student.rangiffler.controller.query;

import io.student.rangiffler.data.entity.PhotoEntity;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.data.repository.PhotoRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.exception.ResourceNotFoundException;
import io.student.rangiffler.model.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class FeedQueryController {

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    public FeedQueryController(PhotoRepository photoRepository,
                               UserRepository userRepository,
                               CountryRepository countryRepository) {
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
    }

    @SchemaMapping(typeName = "Feed", field = "stat")
    public List<Stat> stat(Feed feed) {
        return List.of(
               new Stat().setCount(1)
                        .setCountry(new Country()
                                .setCode("af")
                                .setName("Afghanistan")
                                .setFlag("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAeCAMAAABpA6zvAAABj1BMVEVHcEynHxIAShsAAAAANhcCAgKQExN7JROyIhMBaDAAAAAAPhoCAgKqIRQASx0AAAABAQEAAAAARxoAViN9Ox55EAipHhCoHRC1KRioHRC2KBicFAqeEwqaEwmaEwpxIhACAgIBaTEAAAAEBAQMDAwBaDABazQCAgIAAAAAAAAAYysBZS4CZC4ARxoAAAAAZikAbCwAaSsAcDAAYycAczEAXyUAdjPMGg0AfTndVUkAeDQAUh")),
                        new Stat()
                        .setCount(2)
                        .setCountry(new Country()
                                .setCode("al")
                                .setName("Albania")
                                .setFlag("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAeCAMAAABpA6zvAAAAz1BMVEVHcEzKAwPYAgLEAADNAADTAQHNAADNAAC+AADSAQHHBAS/AADLAADLAQHQAQHAAADVAwPLAgLUAwPLAQHHCgrLAgLCAADJAQHOAQG+AAC5AADHAADDAAC/AAD/AAAAAADZAADPAADlAADhAADSAADeAADoAAD3AAC9AADWAADCAADMAABzAADHAADuAADrAACOAAAHAAD8AACZAABfAAAcAABnAAB+AABWAABrAADwAAC3AACjAABJAACFAACwAACeAAAqAAA1AABAAACqAADoBy1nAAAAHnRSTlMAL7zXzNjH7QeuIYZVmuSvikBkaB1FZI19ur79+bw4PidYAAABt0lEQVQ4y5XUeXOiMByA4ahVxGPsPfYiCQEDJGC4oRyi9vt/poXd1rIiO933z/DMj2NIAKi7X6yX8nA4lJfr24cZ6DZdTAAYXI8RMpHehEzT1LSxfH17M5t+ocGdLGEGhggRh3rKZ5Q6G4K0Jml8NbkaSxhjjegMIPWEWnnvtSfNTcjGoYqi1nCr/KAzSH8Kg8zJk2jX3Poc2sBsQRykvAz3lBaHxKKK1gtFEsOQ7WPfLURFIt4P8xhCI4AwYSHkFfu+Qv6Gtl+6sOnAIUyLrBfu4hR+5UZR2QtFIdyTjP2ziZvWRMHD08TUbz+jBbQWRFm0S/d/hoYVz3thdWS8eXHoBjwVgd8LbeHRwv0Iw0OQ8yg3+6BX8YD7AruhGiYOja3eicRUMo0fIIw+fN/nWS/8XZlAeEyRMKLM+ydUg+RYiuZJWl9YN7pQ2br7ipytXYRKcuz8wTXEXbjt7o7L8EL/C6mj6s0hgD43Zyeq1VAnGmNMGtW7fSS92HXY1DffZ4L3TrBhPAPMJPlucDo/ZjeP89Xo1aqzGcPMtgzDGM0HAEwW0wun0uzhcb5cPb09rZbrxX2z8gshAJYqhHqCfwAAAABJRU5ErkJggg=="))
        );
    }

    @SchemaMapping(typeName = "Photo", field = "likes")
    public Likes likes(Photo photo) {
        var like = new Like()
                .setUser("mock-user-id")
                .setUsername("mockuser")
                .setCreationDate(LocalDate.now());

        return new Likes()
                .setTotal(1500344343)
                .setLikes(List.of(like));
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

        var photoEntities = photoRepository.findByUserIdOrderByCreatedDateDesc(
                user.getId(), pageable);

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
        var countryFlag = "data:image/png;base64," +
                Base64.getEncoder().encodeToString(entity.getCountry().getFlag());

        return new Photo()
                .setId(entity.getId())
                .setSrc("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(entity.getPhoto()))
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
