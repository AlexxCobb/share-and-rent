package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.DAO.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.interfaces.ItemRequestService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.interfaces.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserMapper userMapper;
    private final UserService userService;

    @Override
    @Transactional
    public ItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) {
        var userDto = userService.getUserById(userId);
        var itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequester(userMapper.toUser(userDto));
        var createdItemRequest = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.toItemRequestDto(createdItemRequest);
    }

    @Override
    public List<ItemRequestDto> getAllRequestsByUser(Long userId) {
        userService.getUserById(userId);
        var itemRequests = itemRequestRepository.findAllRequestsByRequesterIdOrderByCreatedDesc(userId);
        return itemRequestMapper.toItemRequestDtoList(itemRequests);
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        userService.getUserById(userId);
        var itemRequests = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(userId, PageRequest.of(from / size, size));
        return itemRequestMapper.toItemRequestDtoList(itemRequests);
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        userService.getUserById(userId);
        var itemRequest = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос с id = " + requestId + " отсутствует."));
        return itemRequestMapper.toItemRequestDto(itemRequest);
    }
}
