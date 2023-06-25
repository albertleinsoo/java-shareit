package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UserExistsException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Integer id) {
        return UserMapper.toUserDto(userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with this id %d not found", id))));
    }

    public Set<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toSet());
    }

    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.create(user));
    }

    public User update(UserDto userDto, Integer id) {
        UserDto updatedUser = getUserById(id);
        String updatedEmail = userDto.getEmail();
        if (userRepository.allEmails().contains(updatedEmail)) {
            throw new UserExistsException("User with this id- {} and with email- {} is not found and cannot be updated");
        }
        if (updatedEmail != null && !updatedEmail.isBlank()) {
            String oldEmail = updatedUser.getEmail();
            userRepository.allEmails().remove(oldEmail);
            updatedUser.setEmail(updatedEmail);
        }
        String updatedName = userDto.getName();
        if (updatedName != null && !updatedName.isBlank()) {
            updatedUser.setName(updatedName);
        }
        return userRepository.update(UserMapper.toUser(updatedUser), id);
    }

    public void delete(Integer id) {
        userRepository.delete(id);
    }

}
