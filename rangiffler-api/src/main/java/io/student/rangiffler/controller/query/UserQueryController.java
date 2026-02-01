package io.student.rangiffler.controller.query;

import io.student.rangiffler.model.User;
import io.student.rangiffler.utils.GqlQueryPaginationAndSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
@PreAuthorize("IsAuthenticated()")
public class UserQueryController {

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

    @QueryMapping
    public User user(@AuthenticationPrincipal Jwt principal) {
        return User.newBuilder()
    }
}
