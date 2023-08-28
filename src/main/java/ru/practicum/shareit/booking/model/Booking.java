package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    @NotNull(message = "Дата начала бронирования не должна быть null")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @ToString.Exclude
    @NotNull(message = "Вещь, которую бронирует пользователь не должна быть null")
    private Item item;

    @ManyToOne
    @ToString.Exclude
    @NotNull(message = "Пользователь, который осуществляет бронирование не должн быть null")
    private User booker;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Состояние бронирования не должно быть null")
    private BookingStatus status;
}