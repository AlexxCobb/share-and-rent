package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.constants.Constant;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
    private Long itemId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_PATTERN)
    private LocalDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_PATTERN)
    private LocalDateTime end;
}
