package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingDatesDto;
import ru.practicum.shareit.item.dto.CommentDetailsInfoDto;
import ru.practicum.shareit.itemRequest.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "items", schema = "public")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым или содержать только пробельные символы")
    private String name;

    @NotBlank(message = "Описание не должно быть пустым или содержать только пробельные символы")
    private String description;

    @Column(name = "is_available")
    @NotNull(message = "Признак доступности вещи не должен быть null")
    private Boolean available;

    @ManyToOne
    @ToString.Exclude
    @NotNull(message = "Владелец вещи не должно быть null")
    private User owner;

    @OneToOne
    private ItemRequest request;

    @Transient
    private BookingDatesDto lastBooking;

    @Transient
    private BookingDatesDto nextBooking;

    @Transient
    private List<CommentDetailsInfoDto> comments;
}