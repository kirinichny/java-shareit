package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        log.debug("+ getUsers");
        List<User> users = userService.getUsers();
        log.debug("- getUsers: {}", users);
        return users.stream().map(UserMapper::toUserDto).collect(toList());
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.debug("+ getUserById: userId={}", userId);
        User user = userService.getUserById(userId);
        log.debug("- getUserById: {}", user);
        return UserMapper.toUserDto(user);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto user) {
        log.debug("+ createUser: {}", user);
        User createdUser = userService.createUser(UserMapper.toUser(user));
        log.debug("- createUser: {}", createdUser);
        return UserMapper.toUserDto(createdUser);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto user) {
        log.debug("+ updateUser: {}", user);
        user.setId(userId);
        User updatedUser = userService.updateUser(UserMapper.toUser(user));
        log.debug("- updateUser: {}", updatedUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.debug("+ deleteUser: userId={}", userId);
        userService.deleteUser(userId);
        log.debug("- deleteUser");
    }
}