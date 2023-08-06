package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDtoInput;
import ru.practicum.shareit.request.dto.RequestDtoOutput;
import ru.practicum.shareit.request.dto.RequestDtoShortOutput;

import java.util.List;

public interface RequestService {

    /**
     * Добавление запроса
     * @param itemRequestDtoOutput Dto запроса
     * @param userId Id пользователя, создающего запрос
     * @return Dto запроса
     */
    RequestDtoShortOutput add(RequestDtoInput itemRequestDtoOutput, Integer userId);

    /**
     * Получить список запросов пользователя вместе с данными об ответах на них
     * @param userId Id пользователя
     * @return Список Dto запросов
     */
    List<RequestDtoOutput> getByUser(Integer userId);

    /**
     * Получить список запросов, созданных другим пользователям
     * @param from Индекс первого элемента, начиная с 0
     * @param size Количество элементов для отображения
     * @param userId Id пользователя
     * @return Список Dto запросов
     */
    List<RequestDtoOutput> getAll(Integer from, Integer size, Integer userId);

    /**
     * Получить данные об одном конкретном запросе вместе с данными об ответах на него
     * @param requestId Id запроса
     * @param userId Id пользователя
     * @return Dto запроса
     */
    RequestDtoOutput get(Integer requestId, Integer userId);
}
