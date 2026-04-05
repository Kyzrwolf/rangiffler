package io.student.rangiffler.service.impl;

import io.student.rangiffler.data.entity.CountryEntity;
import io.student.rangiffler.data.entity.FriendshipEntity;
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

import java.util.Base64;
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
    @Transactional(readOnly = true)
    public Page<User> friends(String username, Pageable pageable, String searchQuery) {
        return (searchQuery != null && !searchQuery.isBlank())
                ? userRepository.findFriends(username, searchQuery, pageable)
                .map(this::toUserFromProjection)
                : userRepository.findFriends(username, pageable)
                .map(this::toUserFromProjection);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> incomeInvitations(String username, Pageable pageable, String searchQuery) {
        return (searchQuery != null && !searchQuery.isBlank())
                ? userRepository.findIncomeInvitations(username, searchQuery, pageable)
                .map(this::toUserFromProjection)
                : userRepository.findIncomeInvitations(username, pageable)
                .map(this::toUserFromProjection);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> outcomeInvitations(String username, Pageable pageable, String searchQuery) {
        return (searchQuery != null && !searchQuery.isBlank())
                ? userRepository.findOutcomeInvitations(username, searchQuery, pageable)
                .map(this::toUserFromProjection)
                : userRepository.findOutcomeInvitations(username, pageable)
                .map(this::toUserFromProjection);
    }

    @Override
    @Transactional
    public User updateUser(String username, UserInput input) {
        UserEntity userEntity = getRequiredUser(username);

        if (input.getFirstname() != null) {
            userEntity.setFirstname(input.getFirstname());
        }
        if (input.getSurname() != null) {
            userEntity.setLastName(input.getSurname());
        }
        if (input.getAvatar() != null) {
            userEntity.setAvatar(Utils.stringAsBytes(input.getAvatar()));
        }
        if (input.getLocation() != null) {
            CountryEntity country = countryRepository.findByCode(input.getLocation().getCode())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Страна не найдена по коду: %s", input.getLocation().getCode())));
            userEntity.setCountry(country);
        }

        UserEntity saved = userRepository.save(userEntity);
        return entityToUser(saved, null);
    }

    @Override
    @Transactional
    public User addFriend(String username, UUID friendId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friend = getRequiredUser(friendId);

        currentUser.addFriends(FriendshipStatus.PENDING, friend);
        userRepository.save(currentUser);

        return entityToUser(friend, FriendStatus.INVITATION_SENT);
    }

    @Override
    @Transactional
    public User acceptInvitation(String username, UUID friendId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity inviteUser = getRequiredUser(friendId);

        FriendshipEntity invite = currentUser.getFriendshipAddressees()
                .stream()
                .filter(fe -> fe.getRequester().getUsername().equals(inviteUser.getUsername()))
                .findFirst()
                .orElseThrow();

        invite.setStatus(FriendshipStatus.ACCEPTED);
        currentUser.addFriends(FriendshipStatus.ACCEPTED, inviteUser);
        userRepository.save(currentUser);

        return entityToUser(inviteUser, FriendStatus.FRIEND);
    }

    @Override
    @Transactional
    public User declineInvitation(String username, UUID friendId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friendToDecline = getRequiredUser(friendId);

        currentUser.removeInvites(friendToDecline);
        friendToDecline.removeFriends(currentUser);

        userRepository.save(currentUser);
        userRepository.save(friendToDecline);
        return entityToUser(friendToDecline, null);
    }

    @Override
    @Transactional
    public User removeFriend(String username, UUID friendId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friend = getRequiredUser(friendId);

        currentUser.removeFriends(friend);
        currentUser.removeInvites(friend);
        friend.removeFriends(currentUser);
        friend.removeInvites(currentUser);
        userRepository.save(currentUser);
        userRepository.save(friend);
        return entityToUser(friend, null);
    }

    @Override
    public List<Stat> stat(String username, boolean withFriends) {
        return List.of();
    }

    private User entityToUser(UserEntity entity, FriendStatus friendStatus) {
        return new User()
                .setId(entity.getId())
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
                .setFlag(entity.getFlag() != null && entity.getFlag().length > 0
                        ? "data:image/png;base64," + Base64.getEncoder().encodeToString(entity.getFlag())
                        : "");
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
                .setId(projection.id())
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
