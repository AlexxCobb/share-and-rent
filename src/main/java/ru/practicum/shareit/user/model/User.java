package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class User {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String name;
    private String email;
}
