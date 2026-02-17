package io.student.rangiffler.controller.query;

import io.student.rangiffler.model.User;
import io.student.rangiffler.service.UserService;
import io.student.rangiffler.utils.GqlQueryPaginationAndSort;
import org.springframework.data.domain.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
public class UserQueryController {

    private final UserService userService;

    public UserQueryController(UserService userService) {
        this.userService = userService;
    }

    @SchemaMapping(typeName = "User", field = "friends")
    public Page<User> friends(@Argument int page,
                              @Argument int size,
                              @Argument @Nullable List<String> sort,
                              @Argument @Nullable String searchQuery) {

        Pageable pageable = new GqlQueryPaginationAndSort(page, size, sort).pageable();
        return new PageImpl<>(
                List.of(new User()),
                pageable,
                1);
    }

    @SchemaMapping(typeName = "User", field = "incomeInvitations")
    public Slice<User> incomeInvitations(User user,
                                         @Argument int page,
                                         @Argument int size,
                                         @Argument @Nullable String searchQuery) {

        Pageable pageable = PageRequest.of(page, size);
        return new SliceImpl<>(
                List.of(new User()),
                pageable,
                false
        );
    }
    @SchemaMapping(typeName = "User", field = "outcomeInvitations")
    public Slice<User> outcomeInvitations(User user,
                                          @Argument int page,
                                          @Argument int size,
                                          @Argument @Nullable String searchQuery) {

        Pageable pageable = PageRequest.of(page, size);
        return new SliceImpl<>(
                List.of(new User()),
                pageable,
                false);
    }

    @QueryMapping
    public User user(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaimAsString("sub");
        return userService.getUserByUsername(username);
    }

    @QueryMapping
    public Slice<User> users(@AuthenticationPrincipal Jwt principal,
                             @Argument int page,
                             @Argument int size,
                             @Argument @Nullable String searchQuery) {

        Pageable pageable = PageRequest.of(page, size);
        return new SliceImpl<>(
                List.of(new User()),
                pageable,
                false
        );
    }
}
