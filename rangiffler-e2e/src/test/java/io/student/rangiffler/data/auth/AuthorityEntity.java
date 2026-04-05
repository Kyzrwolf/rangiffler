package io.student.rangiffler.data.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthorityEntity {
    private UUID id;
    private String authority;
    private UUID userId;
}
