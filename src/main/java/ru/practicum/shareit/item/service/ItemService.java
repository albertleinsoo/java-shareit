package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

public interface ItemService {
    ItemDto getItemById(Integer id);

    Set<ItemDto> getAllItems(Integer userId);

    ItemDto create(Integer id, ItemDto itemDto);

    ItemDto update(Integer userId, ItemDto itemDto, Integer itemId);

    Set<ItemDto> searchItemByQuery(String query);

}
