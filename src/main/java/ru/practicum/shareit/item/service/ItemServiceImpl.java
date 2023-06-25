package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;

    private UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
       // this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto getItemById(Integer id) {
        return ItemMapper.toItemDto(itemRepository.getItemById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item with this id %d not found", id))));
    }

    @Override
    public Set<ItemDto> getAllItems(Integer userId) {
        if (userId == null) {
            return itemRepository.getAllItems().stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toSet());
        } else {
            return itemRepository.getAllItemsByUserId(userId).stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public ItemDto create(Integer id, ItemDto itemDto) {
        if (itemDto.getDescription() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("name is empty");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("description is empty");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("available is empty");
        }
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with this id %d not found", id)));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(Integer userId, ItemDto itemDto, Integer itemId) {
        Item item = ItemMapper.toItem(itemDto);
        Item updatedItem = ItemMapper.toItem(getItemById(itemId));
        String newDescription = item.getDescription();
        if (newDescription != null && !newDescription.isBlank()) {
            updatedItem.setDescription(newDescription);
        }
        String newName = item.getName();
        if (newName != null && !newName.isBlank()) {
            updatedItem.setName(newName);
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        if (!updatedItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("item not found");
        }
        return ItemMapper.toItemDto(itemRepository.update(updatedItem, itemId));
    }

    @Override
    public Set<ItemDto> searchItemByQuery(String query) {
        if (query.isEmpty()) {
            return new HashSet<>();
        }

        return itemRepository.searchItemByQuery(query).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toSet());
    }

}
