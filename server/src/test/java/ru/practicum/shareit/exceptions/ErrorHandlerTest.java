package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ErrorHandlerTest {

    private String message;
    private Map<String, String> eMap;
    private ErrorHandler handler;

    @BeforeEach
    void setup() {
        String error = "error";
        message = "exception";
        handler = new ErrorHandler();

        eMap = new HashMap<>();
        eMap.put(error, message);
    }

    @Test
    void testDtoIntegrityExceptionValidationException() {
        DtoIntegrityException exception = new DtoIntegrityException(message);
        Assertions.assertEquals(eMap, handler.validationException(exception));
    }

    @Test
    void testUserEmailAlreadyExistsExceptionValidationException() {
        UserEmailAlreadyExistsException exception = new UserEmailAlreadyExistsException(message);
        Assertions.assertEquals(eMap, handler.validationException(exception));
    }

    @Test
    void testItemAccessExceptionValidationException() {
        ItemAccessException exception = new ItemAccessException(message);
        Assertions.assertEquals(eMap, handler.validationException(exception));
    }

    @Test
    void testIllegalItemBookingExceptionValidationException() {
        IllegalItemBookingException exception = new IllegalItemBookingException(message);
        Assertions.assertEquals(eMap, handler.validationException(exception));
    }

    @Test
    void testUnavailableItemBookingExceptionValidationException() {
        UnavailableItemBookingException exception = new UnavailableItemBookingException(message);
        Assertions.assertEquals(eMap, handler.validationException(exception));
    }

    @Test
    void testObjectNotFoundExceptionValidationException() {
        ObjectNotFoundException exception = new ObjectNotFoundException(message);
        Assertions.assertEquals(eMap, handler.validationException(exception));
    }

    @Test
    void testIllegalSearchModeExceptionValidationException() {
        IllegalSearchModeException exception = new IllegalSearchModeException(message);
        Assertions.assertEquals(eMap, handler.validationException(exception));
    }
}
