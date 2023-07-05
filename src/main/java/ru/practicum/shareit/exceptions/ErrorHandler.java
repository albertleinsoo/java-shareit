package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(final NotFoundException e) {
        log.info("404: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler({UserExistsException.class,
            EmailExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleExistsException(final RuntimeException e) {
        log.debug("409: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(final ValidationException e) {
        log.debug("400: {}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(final RuntimeException e) {
        log.debug("500: {}", e.getMessage());
        return e.getMessage();
    }

}
