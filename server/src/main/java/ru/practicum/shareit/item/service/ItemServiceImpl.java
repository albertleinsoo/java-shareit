package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDtoWithRequestId add(Integer userId, ItemDtoWithRequestId itemDtoWithRequestId) {
        validateItemDto(itemDtoWithRequestId);

        Item item = itemMapper.toItem(itemDtoWithRequestId);

        if (itemDtoWithRequestId.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemDtoWithRequestId.getRequestId())
                    .orElseThrow(() -> new ObjectNotFoundException("Ошибка запроса. Request с id = " + itemDtoWithRequestId.getRequestId() + " не существует")));
        }

        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User с id = " + userId + " не найден.")));
        item = itemRepository.save(item);

        ItemDtoWithRequestId itemDtoOutput = itemMapper.toItemDtoWithRequestId(item);
        itemDtoOutput.setRequestId(itemDtoWithRequestId.getRequestId());
        return itemDtoOutput;
    }

    @Override
    public ItemDto update(Integer itemId, Integer userId, ItemDto itemDto) {
        validateUser(userId);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Ошибка запроса бронирования. Предмет с id = " + itemId + " не существует."));

        if (!Objects.equals(userId, item.getOwner().getId())) {
            throw new ItemAccessException("Ошибка обновления предмета. Только владелец может обновлять предмет.");
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        item = itemRepository.save(item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDtoExtended get(Integer itemId, Integer userId) {

        Sort sort = Sort.by("start").descending();

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ObjectNotFoundException("Ошибка запроса бронирования. Предмет с id = " + itemId + " не существует."));
        ItemDto itemDto = itemMapper.toItemDto(item);

        List<CommentOutputDto> itemComments = commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::toCommentOutputDto).collect(Collectors.toList());

        ItemDtoExtended itemDtoExtended = new ItemDtoExtended(itemDto, itemComments);

        if (Objects.equals(userId, item.getOwner().getId())) {
            Booking last = bookingRepository.findFirst1ByItemIdAndStartIsBeforeAndStatusNot(itemId, LocalDateTime.now(), BookingStatus.REJECTED, sort);
            if (last != null) {
                Booking lastBooking = last;
                itemDtoExtended.setLastBooking(bookingMapper.toBookingDtoShortOutput(lastBooking));
            }

            Booking next = bookingRepository.findFirst1ByItemIdAndStartIsAfterAndStatusNot(itemId, LocalDateTime.now(), BookingStatus.REJECTED, sort.ascending());
            if (next != null) {
                Booking nextBooking = next;
                itemDtoExtended.setNextBooking(bookingMapper.toBookingDtoShortOutput(nextBooking));
            }
        }
        return itemDtoExtended;
    }

    @Override
    public List<ItemDtoExtended> getAll(Integer userId) {
        Sort sort = Sort.by("start").descending();

        List<ItemDtoExtended> userItems = itemRepository.findByOwnerIdOrderByIdAsc(userId).stream()
                .map(itemMapper::toItemDto)
                .map(itemDto -> new ItemDtoExtended(itemDto, null))
                .map(item -> {
                    Booking last = bookingRepository.findFirst1ByItemIdAndStartIsBeforeAndStatusNot(item.getId(), LocalDateTime.now(), BookingStatus.REJECTED, sort);
                    if (last != null) {
                        Booking lastBooking = last;
                        item.setLastBooking(bookingMapper.toBookingDtoShortOutput(lastBooking));
                    }

                    Booking next = bookingRepository.findFirst1ByItemIdAndStartIsAfterAndStatusNot(item.getId(), LocalDateTime.now(), BookingStatus.REJECTED, sort.ascending());
                    if (next != null) {
                        Booking nextBooking = next;
                        item.setNextBooking(bookingMapper.toBookingDtoShortOutput(nextBooking));
                    }
                    return item;
                }).collect(Collectors.toList());

        return userItems;
    }

    @Override
    public List<ItemDto> search(Integer userId, String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }

        Boolean available = true;

        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text,
                        text, available).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentOutputDto addComment(Integer itemId, Integer userId, Comment commentInput) {
        validateUser(userId);

        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if ((optionalItem.isEmpty())) {
            throw new ObjectNotFoundException("Booking с id = " + itemId + " Не найден.");
        }
        Item item = optionalItem.get();

        Sort sort = Sort.by("start").descending();

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);

        List<Booking> bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), pageable).toList();
        if (bookings.isEmpty()) {
            throw new UnavailableItemBookingException("Ошибка добавления комментария. Нет завершённых бронирований.");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new IllegalItemBookingException("Ошибка добавления комментария. Владельцы не могут комментировать свой предмет.");
        }

        commentInput.setItem(item);
        commentInput.setAuthor(userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Ошибка запроса бронирования. Пользователь с id = " + userId + " не существует.")));

        Comment comment = commentRepository.save(commentInput);

        return commentMapper.toCommentOutputDto(comment);
    }

    private void validateItemDto(ItemDtoWithRequestId itemDtoWithRequestId) {
        if (itemDtoWithRequestId.getAvailable() == null || itemDtoWithRequestId.getName() == null || itemDtoWithRequestId.getName().isEmpty()
                || itemDtoWithRequestId.getDescription() == null) {
            throw new DtoIntegrityException("Ошибка запроса предмета. Название, оприсание и статус предмета не должны быть null.");
        }
        if (itemDtoWithRequestId.getRequestId() != null) {
            if (!requestRepository.existsById(itemDtoWithRequestId.getRequestId())) {
                throw new ObjectNotFoundException("Ошибка запроса предмета. Запрос с id = "
                        + itemDtoWithRequestId.getRequestId() + " не существует.");
            }
        }
    }

    private void validateUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Ошибка запроса бронирования. Пользователь с id = " + userId + " не существует.");
        }
    }
}
