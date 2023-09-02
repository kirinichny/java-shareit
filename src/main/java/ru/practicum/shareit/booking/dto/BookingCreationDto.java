package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.validation.StartBeforeEndDateValid;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@StartBeforeEndDateValid
public class BookingCreationDto {
    private Long id;

    @FutureOrPresent(message = "Дата начала бронирования должна быть в будущем или настоящем")
    @NotNull(message = "Дата начала бронирования не должна быть null")
    private LocalDateTime start;

    @Future(message = "Дата окончания бронирования должна быть в будущем")
    @NotNull(message = "Дата окончания бронирования не должна быть null")
    private LocalDateTime end;

    @NotNull(message = "Идентификатор вещи, которую бронирует пользователь не должн быть null")
    private Long itemId;
}