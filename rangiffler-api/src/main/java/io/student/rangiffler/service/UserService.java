package io.student.rangiffler.service;

import io.student.rangiffler.data.UserEntity;
import io.student.rangiffler.model.Country;
import io.student.rangiffler.model.User;
import io.student.rangiffler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + username));

        return mapToUser(userEntity);
    }

    private User mapToUser(UserEntity entity) {
        var user = new User()
                .setId(entity.getId())
                .setUsername(entity.getUsername())
                .setFirstname(entity.getFirstname())
                .setSurname(entity.getLastName());

        if (entity.getAvatar() != null) {
            var avatarBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(entity.getAvatar());
            user.setAvatar(avatarBase64);
        }

        if (entity.getCountry() != null) {
            var countryEntity = entity.getCountry();
            var countryFlag = "data:image/png;base64," + Base64.getEncoder().encodeToString(countryEntity.getFlag());
            user.setLocation(new Country()
                    .setCode(countryEntity.getCode())
                    .setName(countryEntity.getName())
                    .setFlag(countryFlag));
        }

        return user;
    }
}
