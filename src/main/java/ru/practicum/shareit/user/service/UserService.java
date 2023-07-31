package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    /**
     * Добавление пользователя
     * @param userDto dto добавляемого пользователя
     * @return Добавленный dto пользователь
     */
    UserDto add(UserDto userDto);

    /**
     * Обновление пользователя
     * @param id Id обновляемого пользователя
     * @param userDto Dto пользователя с новыми данными
     * @return Обновлённый dto пользователя
     */
    UserDto update(int id, UserDto userDto);

    /**
     * Получение пользователя
     * @param id Id запрашеваемого пользователя
     * @return Dto запрошенного пользователя
     */
    UserDto get(int id);

    /**
     * Удаление пользователя
     * @param id Id удаляемого пользователя
     */
    void delete(int id);

    /**
     * Получение списка dto всех пользователей
     * @return Список Dto всех пользователей
     */
    List<UserDto> getAll();
}
