package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.enums.Status;

import java.time.LocalDateTime;

@Data
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long bookerId;
    private Long itemId;
    private Status status;
    private String review;
}
