package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String OWNER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDtoWithRequestId add(@RequestHeader(OWNER) @NotNull Integer userId, @Valid @RequestBody ItemDtoWithRequestId itemDto) {
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable @Positive Integer itemId,
                          @RequestHeader(OWNER) @NotNull Integer userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.update(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable @NotNull @Positive Integer itemId,
                       @RequestHeader(OWNER) @NotNull Integer userId) {
        return itemService.get(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoExtended> getAll(@RequestHeader(OWNER) @NotNull Integer userId) {
        return itemService.getAll(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(OWNER) @NotNull Integer userId, @RequestParam("text") String text) {
        return itemService.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutputDto addComment(@PathVariable @NotNull Integer itemId,
                                       @RequestHeader(OWNER) @NotNull Integer userId,
                                       @Valid @RequestBody Comment comment) {
        return itemService.addComment(itemId, userId, comment);
    }
}
