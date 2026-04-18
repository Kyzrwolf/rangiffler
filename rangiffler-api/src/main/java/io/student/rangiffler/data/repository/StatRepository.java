package io.student.rangiffler.data.repository;

import io.student.rangiffler.data.entity.StatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatRepository extends JpaRepository<StatEntity, UUID> {
}
