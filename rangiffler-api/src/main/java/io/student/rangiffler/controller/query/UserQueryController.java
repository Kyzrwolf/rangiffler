package io.student.rangiffler.controller.query;

import io.student.rangiffler.model.User;
import io.student.rangiffler.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserServiceImpl userService;

    @Autowired
    public UserQueryController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @SchemaMapping(typeName = "User", field = "friends")
    public Page<User> friends(User user,
                              @Argument int page,
                              @Argument int size,
                              @Argument @Nullable List<String> sort,
                              @Argument @Nullable String searchQuery) {

        return userService.friends(
                user.getUsername(),
                PageRequest.of(page, size),
                searchQuery
        );
    }

    @SchemaMapping(typeName = "User", field = "incomeInvitations")
    public Slice<User> incomeInvitations(User user,
                                         @Argument int page,
                                         @Argument int size,
                                         @Argument @Nullable String searchQuery) {
        return userService.incomeInvitations(
                user.getUsername(),
                PageRequest.of(page, size),
                searchQuery
        );
    }

    @SchemaMapping(typeName = "User", field = "outcomeInvitations")
    public Slice<User> outcomeInvitations(User user,
                                          @Argument int page,
                                          @Argument int size,
                                          @Argument @Nullable String searchQuery) {
        return userService.outcomeInvitations(
                user.getUsername(),
                PageRequest.of(page, size),
                searchQuery
        );
    }

    @QueryMapping
    public User user(@AuthenticationPrincipal Jwt principal) {
        return userService.createNewUserIfNotPresent(principal.getClaim("sub"));
    }

    @QueryMapping
    public Slice<User> users(@AuthenticationPrincipal Jwt principal,
                             @Argument int page,
                             @Argument int size,
                             @Argument @Nullable String searchQuery) {
        return userService.allUsers(
                principal.getClaim("sub"),
                PageRequest.of(page, size),
                searchQuery
        );
    }
}
