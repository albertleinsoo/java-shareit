package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {

    private final Map<Integer, Item> itemsMap = new HashMap<>();
    private Integer id = 0;

    public Optional<Item> getItemById(Integer id) {
        return Optional.ofNullable(itemsMap.get(id));
    }

    public Set<Item> getAllItems() {
        return new HashSet<>(itemsMap.values());
    }

    public Item create(Item item) {
        item.setId(++id);
        itemsMap.put(item.getId(), item);
        return item;
    }

    public Item update(Item item, Integer id) {
        itemsMap.put(id, item);
        return item;
    }

    public Set<Item> searchItemByQuery(String query) {
        return itemsMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(query.toLowerCase())
                        || item.getDescription().toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toSet());
    }

    public Set<Item> getAllItemsByUserId(Integer userId) {
        return itemsMap.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toSet());
    }

}
