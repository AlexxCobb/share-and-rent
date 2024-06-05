package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.DAO.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ItemDAO itemDAO;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        userService.getUserById(userId);
        var item = itemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        return itemMapper.toItemDto(itemDAO.createItem(item));
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        userService.getUserById(userId);
        var item = itemDAO.findItem(itemId).orElseThrow(
                () -> {
                    log.error("Вещь с таким id: " + itemId + ", отсутствует.");
                    return new NotFoundException("Вещь с таким id: " + itemId + ", отсутствует.");
                });
        if (!item.getOwnerId().equals(userId)) {
            log.error("Данный пользователь " + userId + " не является владельцем вещи с id: " + itemId);
            throw new NotFoundException("Данный пользователь " + userId + " не является владельцем вещи с id: " + itemId);
        }
        itemMapper.updateItemFromItemDto(itemDto, item);
        return itemMapper.toItemDto(itemDAO.updateItem(item));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        var item = itemDAO.findItem(itemId).orElseThrow(
                () -> {
                    log.error("Вещь с таким id: " + itemId + ", отсутствует.");
                    return new NotFoundException("Вещь с таким id: " + itemId + ", отсутствует.");
                });
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        userService.getUserById(userId);
        return itemDAO.getUserItems(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemToRent(String text) {
        if (text == null) {
            log.error("Параметр для поиска вещи пустой.");
            throw new BadRequestException("Параметр для поиска вещи пустой.");
        }
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemDAO.findItemToRent(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
