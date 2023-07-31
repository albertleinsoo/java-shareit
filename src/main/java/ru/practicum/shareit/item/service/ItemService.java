package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentOutputDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;

import java.util.List;

public interface ItemService {
    /**
     * Добавление предмета
     * @param userId Id пользователя владельца
     * @param itemDto Dto предмета
     * @return Dto добавленной вещи
     */
    ItemDto add(Integer userId, ItemDto itemDto);

    /**
     * Обновление вещи
     * @param itemId Id вещи
     * @param userId Id владельца
     * @param itemDto Dto с обновлёнными данными предмета
     * @return Dto обовлённого предмета
     */
    ItemDto update(Integer itemId, Integer userId, ItemDto itemDto);

    /**
     * Получение предмета по id
     * @param itemId Id предмета
     * @param userId Id владельца
     * @return Dto полученного предмета
     */
    ItemDto get(Integer itemId, Integer userId);

    /**
     * Получение всех предметов по владельцу
     * @param userId Id полльзователя
     * @return Список всех предметов пользователя
     */
    List<ItemDtoExtended> getAll(Integer userId);

    /**
     * Поиск предмета по тексту
     * @param userId Id пользователя
     * @param text текст для поиска
     * @return Список вещей, найденных по текстку
     */
    List<ItemDto> search(Integer userId, String text);

    /**
     * Добавление комментария к предмету
     * @param itemId Id предмета
     * @param userId Id пользователя
     * @param comment Комментарий
     * @return Dto комментария
     */
    CommentOutputDto addComment(Integer itemId, Integer userId, Comment comment);

    /**
     * Получение предмета с комментарием
     * @param itemId Id предмета
     * @param userId Id пользователя
     * @return Dto предмета с комментарием
     */
    ItemDtoExtended getItemWithComments(Integer itemId, Integer userId);
}
