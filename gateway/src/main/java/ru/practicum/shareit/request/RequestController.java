package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public Object add(@RequestHeader("X-Sharer-User-Id") @NotNull Integer userId,
                                      @Valid @RequestBody RequestDto requestDto) {

        log.info("Add request with userId={}, requestDto={}", userId, requestDto);

        return requestClient.add(userId, requestDto);
    }

    @GetMapping
    public Object getByUser(@RequestHeader("X-Sharer-User-Id") @NotNull Integer userId) {

        log.info("Get requests by userId={}", userId);

        return requestClient.getByUser(userId);
    }

    @GetMapping("/all")
    public Object getAll(@RequestParam(name = "from", required = false) @PositiveOrZero Integer from,
                                         @RequestParam(name = "size", required = false) @Positive Integer size,
                                         @RequestHeader("X-Sharer-User-Id") @NotNull Integer userId) {

        log.info("Get all requests by userId={}, from={}, size={}", userId, from, size);

        return requestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public Object get(@PathVariable @NotNull @Positive Integer requestId,
                                      @RequestHeader("X-Sharer-User-Id") @NotNull Integer userId) {

        log.info("Get request by requestId={}, userId={}", requestId, userId);

        return requestClient.get(userId, requestId);
    }
}
