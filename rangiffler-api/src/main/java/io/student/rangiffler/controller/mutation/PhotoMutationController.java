package io.student.rangiffler.controller.mutation;

import io.student.rangiffler.model.Photo;
import io.student.rangiffler.model.PhotoInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
@PreAuthorize("IsAuthenticated()")
public class PhotoMutationController {

    @MutationMapping
    public Photo photo(@Argument PhotoInput photoInput) {

    }
}
