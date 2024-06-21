package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.DAO.ItemRequestDAO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.interfaces.ItemRequestService;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestDAO itemRequestDAO;

    @Override
    public ItemRequest getItemRequestById(Long id) {
        return itemRequestDAO.findItemRequestById(id).orElseThrow(() ->
                new NotFoundException("Запрос с ID: " + id + "не найден."));
    }
}
