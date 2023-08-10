package ru.practicum.shareit.user.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserAlreadyExistsException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorageImpl implements UserStorage {
    private final Map<Long, User> usersData = new HashMap<>();
    private Long lastUserId = 0L;

    @Override
    public User getUserById(Long userId) {
        verifyUserExists(userId);
        return usersData.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(usersData.values());
    }

    @Override
    public Long createUser(User user) {
        verifyUserWithEmailExists(user.getEmail());

        final Long id = generateNewUserId();
        user.setId(id);

        usersData.put(id, user);

        return user.getId();
    }

    @Override
    public Long updateUser(User updatedUser) {
        final Long userId = updatedUser.getId();
        final String email = updatedUser.getEmail();

        verifyUserExists(userId);
        verifyUserWithEmailExists(email, userId);

        User user = usersData.get(userId);

        user.setEmail(email);
        user.setName(updatedUser.getName());

        return userId;
    }

    @Override
    public void deleteUser(long userId) {
        usersData.remove(userId);
    }

    @Override
    public boolean isUserExists(Long userId) {
        return usersData.containsKey(userId);
    }

    @Override
    public void verifyUserExists(Long userId) throws NotFoundException {
        final String errorMessage = "Пользователь #" + userId + " не найден.";

        if (!isUserExists(userId)) {
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public boolean isUserWithEmailExists(String email, Long excludedUserId) {
        return usersData.values().stream()
                .filter(user -> !user.getId().equals(excludedUserId))
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public void verifyUserWithEmailExists(String email, Long excludedUserId) throws NotFoundException {
        final String errorMessage = "Пользователь с адресом " + email + " уже существует.";

        if (isUserWithEmailExists(email, excludedUserId)) {
            throw new UserAlreadyExistsException(errorMessage);
        }
    }

    @Override
    public void verifyUserWithEmailExists(String email) throws NotFoundException {
        verifyUserWithEmailExists(email, null);
    }

    private Long generateNewUserId() {
        return ++lastUserId;
    }
}