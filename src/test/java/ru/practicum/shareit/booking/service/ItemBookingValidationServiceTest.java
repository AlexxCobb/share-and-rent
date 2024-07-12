package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemBookingValidationServiceTest {

    private ItemBookingValidationService service;

    @Mock
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        service = new ItemBookingValidationService(userService, itemService, itemRepository);
    }

    @Test
    void isUserHaveItemsWithException() {
        assertThrows(BadRequestException.class, () -> service.isUserHaveItems(any()));
    }

    @Test
    void isUserHaveItems() {
        var user1 = new User();
        user1.setId(1L);
        var items = List.of(new ItemDto(), new ItemDto());
        when(itemService.getUserItems(any(), any(), any())).thenReturn(items);
        service.isUserHaveItems(any());
        verify(itemService).getUserItems(any(), any(), any());
    }

    @Test
    void isItemAvailableWithException400() {
        var item = new Item();
        item.setId(1L);
        item.setAvailable(false);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertThrows(BadRequestException.class, () -> service.isItemAvailable(1L));
    }

    @Test
    void isItemAvailableWithException404() {
        assertThrows(NotFoundException.class, () -> service.isItemAvailable(any()));
    }

    @Test
    void isItemAvailable() {
        var item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        var result = service.isItemAvailable(item.getId());
        assertEquals(item, result);
    }
}