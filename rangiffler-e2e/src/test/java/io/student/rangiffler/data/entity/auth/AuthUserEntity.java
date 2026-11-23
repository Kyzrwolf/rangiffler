package io.student.rangiffler.data.entity.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AuthUserEntity {
    @Nonnull
    private UUID id;
    @Nonnull
    private String username;
    @Nonnull
    private String password;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    @Nonnull
    private List<AuthorityEntity> authorities = new ArrayList<>();
}
