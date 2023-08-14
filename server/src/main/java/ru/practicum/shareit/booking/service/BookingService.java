package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;

import java.util.List;

public interface BookingService {

    /**
     * Добавление бронирования
     * @param userId Id пользователя
     * @param bookingDtoInput Dto бронирования
     * @return Dto бронирования
     */
    BookingDtoOutput add(Integer userId, BookingDtoInput bookingDtoInput);

    /**
     * Подтверждение или отклонение запроса на бронирование
     * @param bookingId Id бронирования
     * @param userId Id пользователя владельца
     * @param isApproved Подтверждение бронирования
     * @return Dto бронирования
     */
    BookingDtoOutput setApprove(Integer bookingId, Integer userId, Boolean isApproved);

    /**
     * Получение данных о конкретном бронировании
     * @param bookingId Id бронирования
     * @param userId Id владельца вещи или создателя бронирования
     * @return Dto бронирования
     */
    BookingDtoOutput get(Integer bookingId, Integer userId);

    /**
     * Получение списка всех бронирований текущего пользователя.
     * @param bookingStatus Статус бронирования
     * @param userId Id пользователя
     * @param from Индекс первого элемента, начиная с 0
     * @param size Количество элементов для отображения
     * @return Список Dto бронирования
     */
    List<BookingDtoOutput> getAll(String bookingStatus, Integer userId, Integer from, Integer size);

    /**
     * Получение списка бронирований для всех вещей текущего пользователя
     * @param bookingStatus Статус бронирования
     * @param userId Id пользователя
     * @param from Индекс первого элемента, начиная с 0
     * @param size Количество элементов для отображения
     * @return Список Dto бронирования
     */
    List<BookingDtoOutput> getAllByOwner(String bookingStatus, Integer userId, Integer from, Integer size);
}
