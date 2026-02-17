package io.student.rangiffler.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "country")
@Getter
@Setter
public class CountryEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "code", nullable = false, columnDefinition = "VARCHAR(50)")
    private String code;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "flag", columnDefinition = "LONGBLOB")
    private byte[] flag;
}
