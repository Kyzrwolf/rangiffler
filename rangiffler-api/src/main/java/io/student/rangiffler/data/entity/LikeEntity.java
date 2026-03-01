package io.student.rangiffler.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "`like`")
public class LikeEntity {
    @Id
    @Size(max = 16)
    @ColumnDefault("(uuid_to_bin(uuid(), true))")
    @Column(name = "id", nullable = false, length = 16)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;


}