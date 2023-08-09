package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым или содержать только пробельные символы")
    private String name;

    @NotBlank(message = "Описание не должно быть пустым или содержать только пробельные символы")
    private String description;

    @NotNull(message = "Признак доступности вещи не должен быть null")
    private Boolean available;

    private Long requestId;
}