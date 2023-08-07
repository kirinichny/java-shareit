package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING("ожидает одобрения"),
    APPROVED("бронирование подтверждено владельцем"),
    REJECTED("бронирование отклонено владельцем"),
    CANCELED("бронирование отменено создателем");

    private final String name;

    BookingStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}