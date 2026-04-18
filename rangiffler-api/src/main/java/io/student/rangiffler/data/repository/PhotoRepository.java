package io.student.rangiffler.data.repository;

import io.student.rangiffler.data.entity.PhotoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {

    interface CountryPhotoCount {
        UUID getCountryId();
        long getPhotoCount();
    }

    @EntityGraph(attributePaths = {"country"})
    Slice<PhotoEntity> findByUserIdOrderByCreatedDateDesc(UUID userId, Pageable pageable);

    @Query("""
            SELECT p.country.id as countryId, COUNT(p) as photoCount
            FROM PhotoEntity p WHERE p.userId = :userId
            GROUP BY p.country.id
            """)
    List<CountryPhotoCount> countByCountryForUser(@Param("userId") UUID userId);

    @Query("""                                                                                                                                                                                                                        
            SELECT p.country.id as countryId, COUNT(p) as photoCount
            FROM PhotoEntity p
            WHERE p.userId = :userId
               OR p.userId IN (
                   SELECT f.addressee.id FROM FriendshipEntity f
                   WHERE f.requester.id = :userId AND f.status = 'ACCEPTED'
               )
               OR p.userId IN (
                   SELECT f.requester.id FROM FriendshipEntity f
                   WHERE f.addressee.id = :userId AND f.status = 'ACCEPTED'
               )
            GROUP BY p.country.id
            """)
    List<CountryPhotoCount> countByCountryForUserWithFriends(@Param("userId") UUID userId);

    @Query("""                                                                                                                                                                                                                        
      SELECT p FROM PhotoEntity p
      WHERE p.userId = :userId
         OR p.userId IN (
             SELECT f.addressee.id FROM FriendshipEntity f
             WHERE f.requester.id = :userId AND f.status = 'ACCEPTED'
         )
         OR p.userId IN (
             SELECT f.requester.id FROM FriendshipEntity f
             WHERE f.addressee.id = :userId AND f.status = 'ACCEPTED'
         )
      ORDER BY p.createdDate DESC
      """)
    Slice<PhotoEntity> findByUserIdAndFriendsOrderByCreatedDateDesc(@Param("userId") UUID userId, Pageable pageable);
}
