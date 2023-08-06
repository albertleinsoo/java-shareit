package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentOutputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {

    /**
     * Добавление предмета
     * @param userId Id пользователя владельца
     * @param itemDtoWithRequestId Dto предмета с id запросом
     * @return Dto добавленной вещи
     */
    ItemDtoWithRequestId add(Integer userId, ItemDtoWithRequestId itemDtoWithRequestId);

    /**
     * Обновление предмета
     * @param itemId Id предмета
     * @param userId Id пользователя
     * @param itemDto Dto предмета
     * @return Dto предмета
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
}
