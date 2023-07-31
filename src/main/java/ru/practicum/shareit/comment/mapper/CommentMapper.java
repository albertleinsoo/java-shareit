package ru.practicum.shareit.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentOutputDto;
import ru.practicum.shareit.comment.model.Comment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto commentDto);

    @Mapping(target = "authorName", source = "comment.author.name")
    CommentOutputDto toCommentOutputDto(Comment comment);
}
