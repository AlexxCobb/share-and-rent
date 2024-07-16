package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.DAO.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.interfaces.BookingService;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserMapper userMapper;

    @Test
    void createBookingByUser() {
        var userDto = createUserDto("name@ya.ru");
        var ownerDto = createUserDto("othername@ya.ru");
        var createdUser = userService.addUser(userDto);
        var createdOwner = userService.addUser(ownerDto);
        var owner = userMapper.toUser(createdOwner);
        var item = createItem(owner);
        var createdItem = itemRepository.save(item);
        var bookingReqDto = createBookingRequestDto(LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        var resultBookingDto = bookingService.createBookingByUser(bookingReqDto, createdUser.getId(), item);
        assertEquals(Status.WAITING, resultBookingDto.getStatus());
    }

    private UserDto createUserDto(String mail) {
        return new UserDto(null, "user", mail);
    }

    private Item createItem(User user) {
        return new Item(null, "Spoon", "description", true, user, null);
    }

    private BookingRequestDto createBookingRequestDto(LocalDateTime start, LocalDateTime end) {
        return new BookingRequestDto(null, start, end);
    }
}
