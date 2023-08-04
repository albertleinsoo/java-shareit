package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private static final String OWNER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOutput add(@RequestHeader(OWNER) @NotNull Integer userId,
                                                @Valid @RequestBody BookingDtoInput bookingDtoInput) {
        return bookingService.add(userId, bookingDtoInput);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutput setApprove(@PathVariable @NotNull Integer bookingId,
                                                       @RequestHeader(OWNER) @NotNull Integer ownerId,
                                                       @RequestParam("approved") @NotNull Boolean isApproved) {
        return bookingService.setApprove(bookingId, ownerId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutput get(@PathVariable @NotNull Integer bookingId,
                                                @RequestHeader(OWNER) @NotNull Integer userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoOutput> getAll(@RequestParam(name = "state", defaultValue = "ALL", required = false) String searchMode,
                                                         @RequestHeader(OWNER) @NotNull Integer userId) {
        return bookingService.getAll(searchMode, userId);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutput> getAllByOwner(@RequestParam(name = "state", defaultValue = "ALL", required = false) String searchMode,
                                                                @RequestHeader(OWNER) @NotNull Integer userId) {
        return bookingService.getAllByOwner(searchMode, userId);
    }
}
