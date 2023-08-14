package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public Object add(@RequestBody @Valid UserDto userDto) {

        log.info("Add user with userDto={}", userDto);

        return userClient.add(userDto);
    }

    @PatchMapping("/{userId}")
    public Object update(@PathVariable @Positive int userId,
                                         @RequestBody @Valid UserDto userDto) {

        log.info("Update user with userId={}, userDto={}", userId, userDto);

        return userClient.update(userDto, userId);
    }

    @GetMapping("/{userId}")
    public Object get(@PathVariable @Positive int userId) {

        log.info("Get user by userId={}", userId);

        return userClient.get(userId);
    }

    @DeleteMapping("/{userId}")
    public Object delete(@PathVariable @Positive int userId) {

        log.info("Delete user by userId={}", userId);

        return userClient.delete(userId);
    }

    @GetMapping
    public Object getAll() {

        log.info("Get all users");

        return userClient.getAll();
    }
}
