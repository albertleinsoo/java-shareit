package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDtoWithRequestId add(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                    @RequestBody ItemDtoWithRequestId itemDto) {
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable Integer itemId,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId,
                                          @RequestBody ItemDto itemDto) {
        return itemService.update(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable Integer itemId,
                                       @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.get(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoExtended> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @RequestParam("text") String text) {
        return itemService.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutputDto addComment(@PathVariable Integer itemId,
                                                       @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                       @RequestBody Comment comment) {
        return itemService.addComment(itemId, userId, comment);
    }
}
