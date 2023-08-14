package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto add(@RequestBody UserDto userDto) {
        return userService.add(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable int id,
                                          @RequestBody UserDto userDto) {
        return userService.update(id, userDto);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable int id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        userService.delete(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }
}
