package io.student.rangiffler.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "statistic")
public class StatEntity {
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;

    @NotNull
    @Column(name = "count", nullable = false)
    private Integer count;


}