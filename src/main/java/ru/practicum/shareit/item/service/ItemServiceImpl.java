package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.interfaces.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.DAO.ItemRepository;
import ru.practicum.shareit.item.comment.DAO.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.interfaces.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final UserService userService;
    @Lazy
    private final BookingService bookingService;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        var userDto = userService.getUserById(userId);
        var user = userMapper.toUser(userDto);
        var item = itemMapper.toItem(itemDto);
        item.setOwner(user);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        userService.getUserById(userId);
        var item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с таким id: " + itemId + ", отсутствует."));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Данный пользователь " + userId + " не является владельцем вещи с id: " + itemId);
        }
        itemMapper.updateItemFromItemDto(itemDto, item);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        userService.getUserById(userId);
        var item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с таким id: " + itemId + ", отсутствует."));
        var itemDto = itemMapper.toItemDto(item);
        if (item.getOwner().getId().equals(userId)) {
            var lastBooking = bookingService.findLastBookingByItemId(item.getId());
            var nextBooking = bookingService.findFutureBookingByItemId(item.getId());
            lastBooking.ifPresent(itemDto::setLastBooking);
            nextBooking.ifPresent(itemDto::setNextBooking);
        }
        var commentsDto = commentMapper.toListCommentsDto(commentRepository.findAllByItemId(itemId));
        itemDto.setComments(commentsDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        userService.getUserById(userId);
        var allItemsDto = itemRepository.findAllByOwnerIdOrderById(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        for (ItemDto itemDto : allItemsDto) {
            var lastBooking = bookingService.findLastBookingByItemId(itemDto.getId());
            var nextBooking = bookingService.findFutureBookingByItemId(itemDto.getId());
            lastBooking.ifPresent(itemDto::setLastBooking);
            nextBooking.ifPresent(itemDto::setNextBooking);
            var commentsDto = commentMapper.toListCommentsDto(commentRepository.findAllByItemId(itemDto.getId()));
            itemDto.setComments(commentsDto);
        }
        return allItemsDto;
    }

    @Override
    public List<ItemDto> searchItemToRent(Long userId, String text) {
        userService.getUserById(userId);
        if (text == null) {
            throw new BadRequestException("Параметр для поиска вещи пустой.");
        }
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItemToRent(text).stream()
                .filter(Item::getAvailable)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        var user = userMapper.toUser(userService.getUserById(userId));
        var comment = commentMapper.toComment(commentDto);

        var item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с таким id: " + itemId + ", отсутствует."));
        if (item.getOwner().getId().equals(userId)) {
            throw new BadRequestException("Данный пользователь " + userId + " является владельцем вещи с id: " + itemId);
        }
        comment.setItem(item);
        var pastBookings = bookingService.getAllBookingsOfUser(userId, String.valueOf(State.PAST));
        if(pastBookings.isEmpty()) {
            throw new BadRequestException("Пользователь c userId" + userId + " не брал вещь в аренду c itemId " + itemId);
        }
        for (BookingResponseDto pastBooking : pastBookings) {
            if (pastBooking.getItem().getId().equals(itemId) && pastBooking.getBooker().getId().equals(userId) && pastBooking.getStatus().equals(Status.APPROVED)) {
                comment.setAuthor(user);
                comment.setCreated(LocalDateTime.now());
                commentRepository.save(comment);
            } else {
                throw new BadRequestException("Пользователь c userId" + userId + " не брал вещь в аренду c itemId " + itemId);
            }
        }
        return commentMapper.toCommentDto(comment);
    }
}
