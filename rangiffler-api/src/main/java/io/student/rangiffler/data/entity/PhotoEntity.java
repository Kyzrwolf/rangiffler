package io.student.rangiffler.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "photo")
public class PhotoEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false, columnDefinition = "BINARY(16)")
    private CountryEntity country;

    @Column(name = "description", columnDefinition = "VARCHAR(255)")
    private String description;

    @NotNull
    @JoinTable(name = "photo_like",
            joinColumns = {
                    @JoinColumn(name = "photo_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "like_id")
            }
    )
    private List<LikeEntity> likes;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Column(name = "created_date", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdDate;
}
