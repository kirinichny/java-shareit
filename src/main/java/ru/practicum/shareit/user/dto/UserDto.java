package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;

    @NotEmpty(message = "Электронный адрес не должен быть пустым")
    @Email(message = "Неверный адрес электронной почты")
    private String email;
}