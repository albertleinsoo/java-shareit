package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
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
    public ItemDtoWithBooking getItemById(@RequestHeader(OWNER) Integer userId,
                                          @PathVariable Integer id) {
        return itemService.getItemById(userId, id);
    }

    @GetMapping
    public List<ItemDtoWithBooking> getAllItems(@RequestHeader(OWNER) Integer ownerId) {
        return itemService.getAllItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByQuery(@RequestParam(name = "text") String query) {
        return itemService.searchItemByQuery(query);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(OWNER) Integer userId, @RequestBody ItemDto itemDto) {
        return itemService.create(userId,itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(OWNER) Integer userId,
                                 @PathVariable Integer itemId, @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(OWNER) Integer userId,
                          @RequestBody ItemDto itemDto, @PathVariable Integer id) {
        return itemService.update(userId,itemDto,id);
    }
}
