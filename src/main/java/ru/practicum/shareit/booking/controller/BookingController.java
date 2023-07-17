package ru.practicum.shareit.booking.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String OWNER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(OWNER) Integer userId, @PathVariable Integer bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingForBooker(@RequestHeader(OWNER) Integer userId,
                                                   @PathParam("state") String state) {
        return bookingService.getAllBookingByUserId(userId, state);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(OWNER) Integer userId,
                                    @RequestBody @Valid BookingDto bookingDto) {
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader(OWNER) Integer userId,
                                    @PathVariable Integer bookingId, @PathParam("approved") @NonNull Boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingForOwner(@RequestHeader(OWNER) Integer userId,
                                                  @PathParam("state") String state) {
        return bookingService.getAllBookingByOwnerId(userId, state);
    }

}
