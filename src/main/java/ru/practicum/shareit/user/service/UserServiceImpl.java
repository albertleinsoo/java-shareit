package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DtoIntegrityException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.UserEmailAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserDto add(@Valid UserDto userDto) {
        validateUserDto(userDto);
        User user = userMapper.toUser(userDto);
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public UserDto update(int userId, @Valid UserDto userDto) {

        Optional<User> optionalUser = userRepository.findById(userId);
        if ((optionalUser.isEmpty())) {
            throw new ObjectNotFoundException("User с id = " + userId + " Не найден.");
        }

        User user = userRepository.findById(userId).get();

        if (userDto.getEmail() != null) {
            if (!Objects.equals(userDto.getEmail(), user.getEmail()) && userWithEmailExists(userDto.getEmail())) {
                throw new UserEmailAlreadyExistsException("Ошибка обновления пользователя. Пользователь с email: " + userDto.getEmail() + " не существует.");
            }
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        user = userRepository.save(user);

        return userMapper.toUserDto(user);
    }

    public UserDto get(int id) {
        Optional<User> user = userRepository.findById(id);
        if ((user.isEmpty())) {
            throw new ObjectNotFoundException("User с id = " + id + " Не найден.");
        }

        return user.map(userMapper::toUserDto).orElse(null);
    }

    public void delete(int id) {
        validateUserById(id);
        userRepository.deleteById(id);
    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    private void validateUserDto(UserDto userDto) {
        if (userDto.getName() == null || userDto.getEmail() == null) {
            throw new DtoIntegrityException("Ошибка. Имя и email пользователя не должны быть null.");
        }
    }

    private void validateUserById(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Ошибка. Пользователь с id = " + userId + " не существует.");
        }
    }

    private boolean userWithEmailExists(String email) {
        return userRepository.findAll().stream().map(User::getEmail).anyMatch(email::equals);
    }
}

