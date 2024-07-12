package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequest toItemRequest(ItemRequestDto itemRequestDto);

    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    @Mapping(source = "itemRequest.id", target = "requestId")
    ItemDto toItemDto(Item item);

    List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> itemRequests);
}
