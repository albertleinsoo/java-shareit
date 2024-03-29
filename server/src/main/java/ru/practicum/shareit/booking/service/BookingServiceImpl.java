package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingSearchMode;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoOutput add(Integer bookerId, BookingDtoInput bookingDtoInput) {
        validateBookingDtoInput(bookingDtoInput);

        Item item = itemRepository.findById(bookingDtoInput.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Ошибка получения предмета."));

        if (!item.getAvailable()) {
            throw new UnavailableItemBookingException("Ошибка создания бронирования. Недоступные предметы не могут быть забронированы.");
        }
        if (item.getOwner().getId().equals(bookerId)) {
            throw new IllegalItemBookingException("Ошибка создания бронирования. Владелец не может забронировать свой предмет.");
        }

        Booking booking = bookingMapper.toBooking(bookingDtoInput);
        booking.setBooker(userRepository.findById(bookerId).orElseThrow(() -> new ObjectNotFoundException("Ошибка создания предмета.")));
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);

        booking = bookingRepository.save(booking);
        return bookingMapper.toBookingDtoOutput(booking);
    }

    @Override
    public BookingDtoOutput setApprove(Integer bookingId, Integer userId, Boolean isApproved) {

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new ObjectNotFoundException("Booking с id = " + bookingId + " не найден.");
        }
        Booking booking = optionalBooking.get();

        Integer itemOwnerId = booking.getItem().getOwner().getId();

        if (!Objects.equals(userId, itemOwnerId)) {
            throw new IllegalItemBookingException("Ошибка изменения статуса бронирования. Только владелец может менять статус.");
        }

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new UnavailableItemBookingException("Статус должен быть = 'WAITING'.");
        }

        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        booking = bookingRepository.save(booking);
        return bookingMapper.toBookingDtoOutput(booking);
    }

    @Override
    public BookingDtoOutput get(Integer bookingId, Integer userId) {
        validateUser(userId);

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new ObjectNotFoundException("Booking с id = " + bookingId + "не найден.");
        }
        Booking booking = optionalBooking.get();

        Integer itemOwnerId = booking.getItem().getOwner().getId();
        Integer bookerId = booking.getBooker().getId();

        if (!userId.equals(itemOwnerId) && !userId.equals(bookerId)) {
            throw new ObjectNotFoundException("Ошибка бронирования. Только владельцы и зпказчики могут просматривать бронирования.");
        }

        return bookingMapper.toBookingDtoOutput(booking);
    }

    public List<BookingDtoOutput> getAll(String bookingSearchMode, Integer userId, Integer from, Integer size) {
        validateUser(userId);

        Sort sort = Sort.by("start").descending();

        Pageable pageable = PageRequest.of(from / size, size, sort);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ObjectNotFoundException("User с id = " + userId + " не найден.");
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime currentTime = LocalDateTime.now();
        BookingSearchMode searchMode = BookingSearchMode.valueOf(bookingSearchMode.toUpperCase());

        switch (searchMode) {
            case ALL:
                return bookingRepository.findByBookerId(userId, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, currentDateTime, currentTime, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findByBookerIdAndEndIsBefore(userId, currentDateTime, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findByBookerIdAndStartIsAfter(userId, currentDateTime, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            default:
                throw new IllegalSearchModeException("Unknown state: " + bookingSearchMode);
        }
    }

    public List<BookingDtoOutput> getAllByOwner(String bookingSearchMode, Integer userId, Integer from, Integer size) {
        validateUser(userId);

        Sort sort = Sort.by("start").descending();

        Pageable pageable = PageRequest.of(from / size, size, sort);

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ObjectNotFoundException("User с id = " + userId + " не найден.");
        }

        switch (bookingSearchMode) {
            case "ALL":
                return bookingRepository.findByItemOwnerId(userId, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findByItemOwnerIdAndEndIsBefore(userId, LocalDateTime.now(), pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findByItemOwnerIdAndStartIsAfter(userId, LocalDateTime.now(), pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.WAITING, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findByItemOwnerIdAndStatus(userId, BookingStatus.REJECTED, pageable).stream()
                        .map(bookingMapper::toBookingDtoOutput)
                        .collect(Collectors.toList());
            default:
                throw new IllegalSearchModeException("Unknown state: " + bookingSearchMode);
        }
    }

    private void validateBookingDtoInput(BookingDtoInput bookingDtoInput) {
        if (bookingDtoInput.getItemId() == null
                || bookingDtoInput.getStart() == null || bookingDtoInput.getEnd() == null
                || bookingDtoInput.getStart().isAfter(bookingDtoInput.getEnd())
                || bookingDtoInput.getEnd().isBefore(bookingDtoInput.getStart())
                || bookingDtoInput.getStart().equals(bookingDtoInput.getEnd())
        ) {
            throw new DtoIntegrityException("Failed to process request. " +
                    "Booker id, item id, start and end time must not be empty. " +
                    "Start and end time must be correct.");
        }
    }

    private void validateUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Ошибка обработки пользователя. User с id = " + userId + " не существует.");
        }
    }

    private void validatePaginationParams(Integer from, Integer size) {
        if ((from != null && from <= 0) || (size != null && size <= 0)) {
            throw new IllegalSearchModeException("Failed to process request. Incorrect pagination parameters.");
        }
    }
}
