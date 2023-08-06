package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDtoInput;
import ru.practicum.shareit.request.dto.RequestDtoOutput;
import ru.practicum.shareit.request.dto.RequestDtoShortOutput;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {

    private static final String OWNER = "X-Sharer-User-Id";
    private final RequestService requestService;

    @PostMapping
    public RequestDtoShortOutput add(@RequestBody RequestDtoInput requestDtoInput,
                                                     @RequestHeader(OWNER) @NotNull Integer userId) {
        return requestService.add(requestDtoInput, userId);
    }

    @GetMapping
    public List<RequestDtoOutput> getByUser(@RequestHeader(OWNER) @NotNull Integer userId) {
        return requestService.getByUser(userId);
    }

    @GetMapping("/all")
    public List<RequestDtoOutput> getAll(@RequestParam(name = "from", required = false) Integer from,
                                                         @RequestParam(name = "size", required = false) Integer size,
                                                         @RequestHeader(OWNER) @NotNull Integer userId) {
        return requestService.getAll(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public RequestDtoOutput get(@PathVariable @NotNull @Positive Integer requestId,
                                                @RequestHeader(OWNER) @NotNull Integer userId) {
        return requestService.get(requestId, userId);
    }
}
