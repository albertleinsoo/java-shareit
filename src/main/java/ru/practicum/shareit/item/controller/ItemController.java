package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER = "X-Sharer-User-Id";

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value = "/{id}")
    public ItemDto getItemById(@PathVariable Integer id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    public Set<ItemDto> getAllItems(@RequestHeader(OWNER) Integer userId) {
        return itemService.getAllItems(userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(OWNER) Integer userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId,itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@Valid @RequestHeader(OWNER) Integer userId,
                          @RequestBody ItemDto itemDto, @PathVariable Integer id) {
        return itemService.update(userId,itemDto,id);
    }

    @GetMapping("/search")
    public Set<ItemDto> searchItemByQuery(@RequestParam(name = "text") String query) {
        return itemService.searchItemByQuery(query);
    }
}
