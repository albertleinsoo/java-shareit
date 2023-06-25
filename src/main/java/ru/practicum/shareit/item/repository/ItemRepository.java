package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

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
        String queryLower = query.toLowerCase();
        return itemsMap.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(queryLower)
                        || item.getDescription().toLowerCase().contains(queryLower)))
                .collect(Collectors.toSet());
    }

    public Set<Item> getAllItemsByUserId(Integer userId) {
        return itemsMap.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toSet());
    }

}
