package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequest {
    private Long id;
    private String description;
    private LocalDateTime created;
    private Long requestorId;
}
