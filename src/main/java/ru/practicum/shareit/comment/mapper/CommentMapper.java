package ru.practicum.shareit.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Component
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getUser().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentDto commentDto, Item item, User user) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .item(item)
                .user(user)
                .created(commentDto.getCreated())
                .build();
    }
}
