package io.student.rangiffler.controller.query;

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
import java.util.List;
import java.util.UUID;

@Controller
public class FeedQueryController {

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
                .setTotal(33)
                .setLikes(List.of(like));
    }

    @SchemaMapping(typeName = "User", field = "photos")
    public Slice<Photo> photos(User user,
                               @Argument int page,
                               @Argument int size) {
        var photo = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAeCAMAAABpA6zvAAAA+VBMVEVHcEyVFQ7S0tKXFg/R0dGYGBCcGRLU1NTW1takHhSeGxLW1tamHxWbFxfKysqhGxPU1NTR0dHT09PU1NTV1dXT09OSFA3///8AMp7MIRbPIxjKHhQAOacANaLRJRkAN6XTJxsAMJzWKx0APKjHHBMAQKsARa7X2NoAPqoCQqzWLiAALpnUKRyfGBCUFA4ISa61IBbm5ubMzMyuJBve3t4CKYgBI3YALJbDKR6uGBBJcbxUL2j5+ftskc7g4+luLFfr6+u+IBYAQqHt7vCpHhVDNnymudyMqdkoOI2JJkOnLTgAOZAAPpQsWrQAPZ1FIVa6yuf09PRBW5RdfvtPAAAAF3RSTlMAueiE0Cal/7jPTITtBwhlH2agL0JV2eqn9WIAAAGcSURBVDjLxdRbV4JAFIbhyfOx0jIlCNM0pBCPlYlCJB7LrP7/j2nvaSYH5aKuei9Zz3yzFhdDCHRcSOcTWD6dOz0m+x0WEoScpEMa1Me0OBZJoD/k6CSXD3W7JKFp9uSjyBt9TDrMhyIYmG683+8SrTMq7ofeppfYnQmADsCn4i/6R2j/YTEOcKTN5mNsPnu3g35CcfZJ4tp82GpdQ7qu1+v1hr4ejuein8zGrbt7Aoo7ZI3GFaYoV631EFvrpVKpAvD1eeu2TFHKSrl8gaFjcGeOsrLIKrcU+uc48zkO/c7Hvh3Cl2fm9pjgHhC+/bhARh2D4BbTqWVZ0+niQmB8DrrfAFw4qxtatWoYxmC1dKxFxTe3WXomsVYMVdFd0lRVRe/AJY6z7MlS7dEkvQF3BnMqdo5JkCzJEIfCnOp3EnV8cTsnMNExuDMnMokyBn9cIKOOQUMduG2W60oBrtYE2HZ7nudlklimiXkeHKjJMnPuY9M0D0jPy8SOoln2fmSjqaNwLNkUMk3zLBwlJJnKBrxK1McOoFg4FcUvX2nLmUOxnjYdAAAAAElFTkSuQmCC";
        var mockPhoto = new Photo()
                .setId(UUID.randomUUID())
                .setSrc(photo)
                .setCountry(new Country()
                        .setCode("ru")
                        .setName("Russian Federation")
                        .setFlag(photo))
                .setDescription("Mock photo")
                .setCreationDate(LocalDate.now())
                .setOwner(true);

        return new SliceImpl<>(List.of(mockPhoto), PageRequest.of(page, size), false);

    }

    @SchemaMapping(typeName = "Feed", field = "photos")
    public Slice<Photo> photos(Feed feed,
                               @Argument int page,
                               @Argument int size) {

        return new SliceImpl<>(List.of(), PageRequest.of(page, size), false);
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
