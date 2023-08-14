package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOutput add(@RequestHeader("X-Sharer-User-Id")Integer userId,
                                                @RequestBody BookingDtoInput bookingDtoInput) {
        return bookingService.add(userId, bookingDtoInput);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutput setApprove(@PathVariable Integer bookingId,
                                                   @RequestHeader("X-Sharer-User-Id") Integer ownerId,
                                                   @RequestParam("approved") Boolean isApproved) {
        return bookingService.setApprove(bookingId, ownerId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutput get(@PathVariable Integer bookingId,
                                                @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoOutput> getAll(@RequestParam(name = "state", defaultValue = "ALL", required = false) String searchMode,
                                                         @RequestParam(name = "from", required = false) Integer from,
                                                         @RequestParam(name = "size", required = false) Integer size,
                                                         @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getAll(searchMode, userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOutput> getAllByOwner(@RequestParam(name = "state", defaultValue = "ALL", required = false) String searchMode,
                                                                @RequestParam(name = "from", required = false) Integer from,
                                                                @RequestParam(name = "size", required = false) Integer size,
                                                                @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.getAllByOwner(searchMode, userId, from, size);
    }
}
