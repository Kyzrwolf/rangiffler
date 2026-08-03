package io.student.rangiffler.service.impl;

import io.student.rangiffler.data.entity.CountryEntity;
import io.student.rangiffler.data.entity.FriendshipEntity;
import io.student.rangiffler.data.entity.FriendshipStatus;
import io.student.rangiffler.data.entity.UserEntity;
import io.student.rangiffler.data.projection.UserWithStatus;
import io.student.rangiffler.data.repository.CountryRepository;
import io.student.rangiffler.data.repository.PhotoRepository;
import io.student.rangiffler.data.repository.UserRepository;
import io.student.rangiffler.exception.ResourceNotFoundException;
import io.student.rangiffler.model.*;
import io.student.rangiffler.service.api.UserService;
import io.student.rangiffler.utils.UserMapperUtils;
import io.student.rangiffler.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final UserMapperUtils userMapper;
    private final PhotoRepository photoRepository;

    public UserServiceImpl(UserRepository userRepository,
                           CountryRepository countryRepository,
                           UserMapperUtils userMapper, PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.userMapper = userMapper;
        this.photoRepository = photoRepository;
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));
        return userMapper.toUser(userEntity, null);
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
        return userMapper.toUser(userEntity, null);
    }

    @Override
    @Transactional(readOnly = true)
    public User currentUser(String username) {
        var userEntity = getRequiredUser(username);
        return userMapper.toUser(userEntity, null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> allUsers(String username, Pageable pageable, String searchQuery) {
        return hasSearch(searchQuery)
                ? userRepository.findAllUsersWithFriendshipStatus(username, searchQuery, pageable)
                .map(this::toUserFromProjection)
                : userRepository.findAllUsersWithFriendshipStatus(username, pageable)
                .map(this::toUserFromProjection);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> friends(String username, Pageable pageable, String searchQuery) {
        return hasSearch(searchQuery)
                ? userRepository.findFriends(username, searchQuery, pageable)
                .map(this::toUserFromProjection)
                : userRepository.findFriends(username, pageable)
                .map(this::toUserFromProjection);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> incomeInvitations(String username, Pageable pageable, String searchQuery) {
        return hasSearch(searchQuery)
                ? userRepository.findIncomeInvitations(username, searchQuery, pageable)
                .map(this::toUserFromProjection)
                : userRepository.findIncomeInvitations(username, pageable)
                .map(this::toUserFromProjection);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> outcomeInvitations(String username, Pageable pageable, String searchQuery) {
        return hasSearch(searchQuery)
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
        return userMapper.toUser(saved, null);
    }

    @Override
    @Transactional
    public User addFriend(String username, UUID friendId) {
        UserEntity currentUser = getRequiredUser(username);
        UserEntity friend = getRequiredUser(friendId);

        currentUser.addFriends(FriendshipStatus.PENDING, friend);
        userRepository.save(currentUser);

        return userMapper.toUser(friend, FriendStatus.INVITATION_SENT);
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
                .orElseThrow(() -> new IllegalStateException("у пользователя %s отсутствуют запрос на дружбу от пользователя %s"
                        .formatted(username, inviteUser.getUsername())));

        invite.setStatus(FriendshipStatus.ACCEPTED);
        currentUser.addFriends(FriendshipStatus.ACCEPTED, inviteUser);
        userRepository.save(currentUser);

        return userMapper.toUser(inviteUser, FriendStatus.FRIEND);
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
        return userMapper.toUser(friendToDecline, null);
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
        return userMapper.toUser(friend, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stat> stat(String username, boolean withFriends) {
        var userEntity = getRequiredUser(username);

        List<PhotoRepository.CountryPhotoCount> counts = withFriends
                ? photoRepository.countByCountryForUserWithFriends(userEntity.getId())
                : photoRepository.countByCountryForUser(userEntity.getId());

        var stats = new ArrayList<Stat>();
        counts.forEach(c -> {
            CountryEntity countryEntity = countryRepository.findById(c.getCountryId())
                    .orElseThrow(() -> new ResourceNotFoundException("такой страны не существует"));
            var country = userMapper.toCountry(countryEntity);

            stats.add(new Stat()
                    .setCount((int) c.getPhotoCount())
                    .setCountry(country));
        });

        return stats;
    }

    private User toUserFromProjection(UserWithStatus projection) {
        Country country = null;
        if (projection.countryId() != null) {
            country = countryRepository.findById(projection.countryId())
                    .map(userMapper::toCountry)
                    .orElse(null);
        }
        return userMapper.toUser(projection, country);
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

    private boolean hasSearch(String searchQuery) {
        return searchQuery != null && !searchQuery.isBlank();
    }
}