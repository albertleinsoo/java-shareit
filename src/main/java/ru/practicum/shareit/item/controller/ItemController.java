package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentOutputDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final static String OWNER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> add(@RequestHeader(OWNER) @NotNull Integer userId, @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok().body(itemService.add(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@PathVariable @Positive Integer itemId,
                                          @RequestHeader(OWNER) @NotNull Integer userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok().body(itemService.update(itemId, userId, itemDto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> get(@PathVariable @NotNull @Positive Integer itemId,
                                       @RequestHeader(OWNER) @NotNull Integer userId) {
        return ResponseEntity.ok().body(itemService.get(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDtoExtended>> getAll(@RequestHeader(OWNER) @NotNull Integer userId) {
        return ResponseEntity.ok().body(itemService.getAll(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestHeader(OWNER) @NotNull Integer userId, @RequestParam("text") String text) {
        return ResponseEntity.ok().body(itemService.search(userId, text));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentOutputDto> addComment(@PathVariable @NotNull Integer itemId,
                                                       @RequestHeader(OWNER) @NotNull Integer userId,
                                                       @Valid @RequestBody Comment comment) {
        return ResponseEntity.ok().body(itemService.addComment(itemId, userId, comment));
    }

    @GetMapping("/{itemId}/comment")
    public ResponseEntity<ItemDtoExtended> getItemWithComments(@PathVariable @NotNull Integer itemId,
                                                               @RequestHeader(OWNER) @NotNull Integer userId) {
        return ResponseEntity.ok().body(itemService.getItemWithComments(itemId, userId));
    }
}
