package ru.practicum.shareit.exceptions;

public class UserExistsException extends RuntimeException {

    public UserExistsException(String error) {
        super(error);
    }
}
