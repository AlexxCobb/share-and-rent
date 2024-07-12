package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.constants.Constant;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ItemRequestDto {
    @PositiveOrZero
    private Long id;
    @NotBlank
    private String description;
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_PATTERN)
    private LocalDateTime created;
    private List<ItemDto> items;
}
