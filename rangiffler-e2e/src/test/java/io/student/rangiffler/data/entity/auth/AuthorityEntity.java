package io.student.rangiffler.data.entity.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AuthorityEntity {
    @Nonnull
    private UUID id;
    @Nonnull
    private String authority;
    @Nonnull
    private UUID userId;
}
