package ru.practicum.shareit.exceptions;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String error) {
        super(error);
    }
}
