package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя не должно быть пустым или содержать только пробельные символы")
    private String name;

    @NotEmpty(message = "Электронный адрес не должен быть пустым")
    @Email(message = "Неверный адрес электронной почты")
    @Column(length = 320, unique = true)
    private String email;
}