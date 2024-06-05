package ru.practicum.shareit.request.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRequestDAO {
    private final Map<Long, ItemRequest> itemRequests;
    private Long generatedId = 1L;

    public ItemRequest addItemRequest (ItemRequest itemRequest) {
        itemRequest.setId(generatedId);
        itemRequests.put(itemRequest.getId(), itemRequest);
        generatedId++;
        return itemRequest;
    }

    public Optional<ItemRequest> findItemRequestById(Long id) {
        return Optional.ofNullable(itemRequests.get(id));
    }
}
