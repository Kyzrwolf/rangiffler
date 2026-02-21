package io.student.rangiffler.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "user")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private CountryEntity country;

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipEntity> friendshipRequests = new ArrayList<>();

    @OneToMany(mappedBy = "addressee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendshipEntity> friendshipAddressees = new ArrayList<>();

    public void addFriends(FriendshipStatus status, io.student.rangiffler.data.entity.UserEntity... friends) {
        List<FriendshipEntity> friendsEntities = Stream.of(friends)
                .map(f -> {
                    FriendshipEntity fe = new FriendshipEntity();
                    fe.setRequester(this);
                    fe.setAddressee(f);
                    fe.setStatus(status);
                    fe.setCreatedDate(new Date());
                    return fe;
                }).toList();
        this.friendshipRequests.addAll(friendsEntities);
    }

    public void removeFriends(io.student.rangiffler.data.entity.UserEntity... friends) {
        List<UUID> idsToBeRemoved = Arrays.stream(friends).map(io.student.rangiffler.data.entity.UserEntity::getId).toList();
        for (Iterator<FriendshipEntity> i = getFriendshipRequests().iterator(); i.hasNext(); ) {
            FriendshipEntity friendsEntity = i.next();
            if (idsToBeRemoved.contains(friendsEntity.getAddressee().getId())) {
                friendsEntity.setAddressee(null);
                i.remove();
            }
        }
    }

    public void removeInvites(io.student.rangiffler.data.entity.UserEntity... invitations) {
        List<UUID> idsToBeRemoved = Arrays.stream(invitations).map(io.student.rangiffler.data.entity.UserEntity::getId).toList();
        for (Iterator<FriendshipEntity> i = getFriendshipAddressees().iterator(); i.hasNext(); ) {
            FriendshipEntity friendsEntity = i.next();
            if (idsToBeRemoved.contains(friendsEntity.getRequester().getId())) {
                friendsEntity.setRequester(null);
                i.remove();
            }
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        io.student.rangiffler.data.entity.UserEntity that = (io.student.rangiffler.data.entity.UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
