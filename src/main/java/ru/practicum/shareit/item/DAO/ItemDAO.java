package ru.practicum.shareit.item.DAO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemDAO {

    private final Map<Long, Item> items;

    private Long generatedId = 1L;

    public Item createItem(Item item) {
        item.setId(generatedId);
        items.put(item.getId(), item);
        generatedId++;
        return item;
    }

    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    public Optional<Item> findItem(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    public List<Item> getUserItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Item> findItemToRent(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase())
                        && item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }
}
