package io.student.rangiffler.repository;

import io.student.rangiffler.data.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    CountryEntity findByCode(String code);

}
