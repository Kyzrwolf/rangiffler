package io.student.rangiffler.data.repository;

import io.student.rangiffler.data.entity.PhotoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {

    @EntityGraph(attributePaths = {"country"})
    Slice<PhotoEntity> findByUserIdOrderByCreatedDateDesc(UUID userId, Pageable pageable);
}
