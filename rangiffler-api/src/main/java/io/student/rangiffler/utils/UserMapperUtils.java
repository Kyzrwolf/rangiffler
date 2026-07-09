package io.student.rangiffler.utils;

import io.student.rangiffler.data.entity.CountryEntity;
import io.student.rangiffler.data.entity.FriendshipStatus;
import io.student.rangiffler.data.entity.UserEntity;
import io.student.rangiffler.data.projection.UserWithStatus;
import io.student.rangiffler.model.Country;
import io.student.rangiffler.model.FriendStatus;
import io.student.rangiffler.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperUtils {

    private final PhotoMapperUtils photoMapper = new PhotoMapperUtils();

    public User toUser(UserEntity entity, FriendStatus friendStatus) {
        return new User()
                .setId(entity.getId())
                .setUsername(entity.getUsername())
                .setFirstname(entity.getFirstname())
                .setSurname(entity.getLastName())
                .setAvatar(Utils.bytesAsString(entity.getAvatar()))
                .setFriendStatus(friendStatus)
                .setLocation(entity.getCountry() != null ? toCountry(entity.getCountry()) : null);
    }

    public User toUser(UserWithStatus projection, Country country) {
        return new User()
                .setId(projection.id())
                .setUsername(projection.username())
                .setFirstname(projection.firstname())
                .setSurname(projection.lastName())
                .setAvatar(Utils.bytesAsString(projection.avatar()))
                .setFriendStatus(calculateFriendStatus(projection.friendshipStatus(), projection.isRequester()))
                .setLocation(country);
    }

    public Country toCountry(CountryEntity entity) {
        return new Country()
                .setCode(entity.getCode())
                .setName(entity.getName())
                .setFlag(entity.getFlag() != null && entity.getFlag().length > 0
                        ? photoMapper.getPngImage(entity.getFlag())
                        : "");
    }

    public FriendStatus calculateFriendStatus(FriendshipStatus status, Boolean isRequester) {
        if (status == FriendshipStatus.ACCEPTED) {
            return FriendStatus.FRIEND;
        }
        if (status == FriendshipStatus.PENDING) {
            return Boolean.TRUE.equals(isRequester)
                    ? FriendStatus.INVITATION_SENT
                    : FriendStatus.INVITATION_RECEIVED;
        }
        return null;
    }
}