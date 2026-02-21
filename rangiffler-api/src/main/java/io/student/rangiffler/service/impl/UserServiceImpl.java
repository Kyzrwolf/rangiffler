package io.student.rangiffler.service.impl;

import io.student.rangiffler.data.entity.CountryEntity;
import io.student.rangiffler.data.entity.FriendshipStatus;
import io.student.rangiffler.data.entity.UserEntity;
import io.student.rangiffler.data.projection.UserWithStatus;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.exception.ResourceNotFoundException;
import io.student.rangiffler.model.*;
import io.student.rangiffler.service.api.UserService;
import io.student.rangiffler.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;

    public UserServiceImpl(UserRepository userRepository, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));
        return entityToUser(userEntity, null);
    }

    @Override
    public User createNewUserIfNotPresent(String username) {
        var userEntity = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    var newUser = new UserEntity()
                            .setUsername(username)
                            .setCountry(countryRepository.findByCode("ru").orElseThrow(() -> new ResourceNotFoundException(
                                    "Страна не найден по коду: ru"
                            )));
                    return userRepository.save(newUser);
                });
        return entityToUser(userEntity, null);
    }

    @Override
    public User currentUser(String username) {
        var userEntity = getRequiredUser(username);
        return entityToUser(userEntity, null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> allUsers(String username, Pageable pageable, String searchQuery) {
        return (searchQuery != null && !searchQuery.isBlank())
                ? userRepository.findAllUsersWithFriendshipStatus(username, searchQuery, pageable)
                .map(this::toUserFromProjection)
                : userRepository.findAllUsersWithFriendshipStatus(username, pageable)
                .map(this::toUserFromProjection);    }

    @Override
    public Page<User> friends(String username, Pageable pageable, String searchQuery) {
        return null;
    }

    @Override
    public Page<User> incomeInvitations(String username, Pageable pageable, String searchQuery) {
        return null;
    }

    @Override
    public Page<User> outcomeInvitations(String username, Pageable pageable, String searchQuery) {
        return null;
    }

    @Override
    public User updateUser(String username, UserInput input) {
        return null;
    }

    @Override
    public User addFriend(String username, UUID friendId) {
        return null;
    }

    @Override
    public User acceptInvitation(String username, UUID friendId) {
        return null;
    }

    @Override
    public User declineInvitation(String username, UUID friendId) {
        return null;
    }

    @Override
    public User removeFriend(String username, UUID friendId) {
        return null;
    }

    @Override
    public List<Stat> stat(String username, boolean withFriends) {
        return List.of();
    }

    private User entityToUser(UserEntity entity, FriendStatus friendStatus) {
        return new User()
                .setId(entity.getId().toString())
                .setUsername(entity.getUsername())
                .setFirstname(entity.getFirstname())
                .setFirstname(entity.getLastName())
                .setAvatar(Utils.bytesAsString(entity.getAvatar()))
                .setFriendStatus(friendStatus)
                .setLocation(entity.getCountry() != null ? entityToCountry(entity.getCountry()) : null);
    }

    private Country entityToCountry(CountryEntity entity) {
        return new Country()
                .setCode(entity.getCode())
                .setName(entity.getName())
                .setFlag(Utils.bytesAsString(entity.getFlag()));
    }


    private UserEntity getRequiredUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Пользователь не найден по username: %s", username)
                ));
    }

    private UserEntity getRequiredUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Пользователь не найден по id: %s", userId)
                ));
    }

    private User toUserFromProjection(UserWithStatus projection) {
        FriendStatus friendStatus = calculateFriendStatus(
                projection.friendshipStatus(),
                projection.isRequester()
        );

        Country country = null;
        if (projection.countryId() != null) {
            var countryEntity = countryRepository.findById(projection.countryId()).orElse(null);
            if (countryEntity != null) {
                country = entityToCountry(countryEntity);
            }
        }

        return new User()
                .setId(projection.id().toString())
                .setUsername(projection.username())
                .setFirstname(projection.firstname())
                .setSurname(projection.lastName())
                .setAvatar(Utils.bytesAsString(projection.avatar()))
                .setFriendStatus(friendStatus)
                .setLocation(country);
    }

    private FriendStatus calculateFriendStatus(FriendshipStatus status, Boolean isRequester) {
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
