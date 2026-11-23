package io.student.rangiffler.data.entity.userdata;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UdUserEntity {
    @Nonnull
    private UUID id;
    @Nonnull
    private String username;
    @Nullable
    private String firstname;
    @Nullable
    private String lastName;
    @Nullable
    private byte[] avatar;
    @Nonnull
    private UUID countryId;
}
