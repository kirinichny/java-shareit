package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingCreationDto {
    private Long id;

    @FutureOrPresent(message = "Дата начала бронирования должна быть в будущем или настоящем")
    @NotNull(message = "Дата начала бронирования не должна быть null")
    private LocalDateTime start;

    @FutureOrPresent(message = "Дата окончания бронирования должна быть в будущем или настоящем")
    @NotNull(message = "Дата окончания бронирования не должна быть null")
    private LocalDateTime end;

    @NotNull(message = "Идентификатор вещи, которую бронирует пользователь не должн быть null")
    private Long itemId;
}