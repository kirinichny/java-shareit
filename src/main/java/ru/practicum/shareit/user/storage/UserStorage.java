package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User getUserById(Long userId);

    List<User> getUsers();

    Long createUser(User user);

    Long updateUser(User user);

    void deleteUser(long userId);

    boolean isUserExists(Long userId);

    void verifyUserExists(Long userId);

    boolean isUserWithEmailExists(String email, Long excludedUserId);

    void verifyUserWithEmailExists(String email, Long excludedUserId);

    void verifyUserWithEmailExists(String email);
}