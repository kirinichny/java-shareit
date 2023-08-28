package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDatesDto;
import ru.practicum.shareit.booking.dto.BookingDetailsInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class BookingMapper {
    public BookingDetailsInfoDto toBookingDetailsDto(Booking booking) {
        return BookingDetailsInfoDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public BookingDatesDto toBookingDatesDto(Booking booking) {
        return BookingDatesDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    public Booking toBooking(BookingCreationDto bookingCreationDto) {
        Item item = Item.builder().id(bookingCreationDto.getItemId()).build();

        return Booking.builder()
                .id(bookingCreationDto.getId())
                .start(bookingCreationDto.getStart())
                .end(bookingCreationDto.getEnd())
                .item(item)
                .build();
    }
}