package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User createUser(User user) {
        return userStorage.getUserById(userStorage.createUser(user));
    }

    @Override
    public User updateUser(User user) {

        User currentUser = getUserById(user.getId());

        if (Objects.isNull(user.getName())) {
            user.setName(currentUser.getName());
        }

        if (Objects.isNull(user.getEmail())) {
            user.setEmail(currentUser.getEmail());
        }

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return userStorage.getUserById(userStorage.updateUser(user));
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }
}