package io.student.rangiffler.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String username;

    @Column(name = "firstname", columnDefinition = "VARCHAR(255)")
    private String firstname;

    @Column(name = "lastName", columnDefinition = "VARCHAR(255)")
    private String lastName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "avatar", columnDefinition = "LONGBLOB")
    private byte[] avatar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false, columnDefinition = "BINARY(16)")
    private CountryEntity country;

    // ... existing code ...
}
