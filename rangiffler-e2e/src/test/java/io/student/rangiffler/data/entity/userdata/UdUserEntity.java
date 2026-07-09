package io.student.rangiffler.data.entity.userdata;

import lombok.Data;

import java.util.UUID;

@Data
public class UdUserEntity {
    private UUID id;
    private String username;
    private String firstname;
    private String lastName;
    private UUID countryId;
}
