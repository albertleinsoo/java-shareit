package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestMapperTest {

    @Test
    void toRequestDtoShortOutput_FromRequest() {
        User user = new User(2, "Shaun", "shaun@ya.ru");
        Request request = new Request(1, "Looking for Balalaika", LocalDateTime.now(), user);

        RequestDtoShortOutput requestDtoShortOutput = RequestMapper.INSTANCE.toRequestDtoShortOutput(request);

        assertNotNull(requestDtoShortOutput);

        assertEquals(request.getId(), requestDtoShortOutput.getId());
        assertEquals(request.getDescription(), requestDtoShortOutput.getDescription());
        assertEquals(request.getCreated(), requestDtoShortOutput.getCreated());
    }

    @Test
    void toRequestDtoOutput_FromRequest() {
        User user = new User(2, "Shaun", "shaun@ya.ru");
        Request request = new Request(1, "Looking for Balalaika", LocalDateTime.now(), user);

        RequestDtoOutput requestDtoOutput = RequestMapper.INSTANCE.toRequestDtoOutput(request);

        assertNotNull(requestDtoOutput);

        assertEquals(request.getId(), requestDtoOutput.getId());
        assertEquals(request.getDescription(), requestDtoOutput.getDescription());
        assertEquals(request.getCreated(), requestDtoOutput.getCreated());
        assertNull(requestDtoOutput.getItems());
    }

    @Test
    void toRequestDto_FromRequest() {
        User user = new User(2, "Shaun", "shaun@ya.ru");
        Request request = new Request(1, "Looking for Balalaika", LocalDateTime.now(), user);

        ItemDto itemDto = new ItemDto(1, "Balalaika", "Brand new balalaika", true);
        ItemDtoWithRequestId item = new ItemDtoWithRequestId(itemDto, 1);
        List<ItemDtoWithRequestId> items = new ArrayList<>(List.of(item));

        RequestDto requestDto = RequestMapper.INSTANCE.toRequestDto(request, items);

        assertNotNull(requestDto);

        assertEquals(request.getId(), requestDto.getId());
        assertEquals(request.getDescription(), requestDto.getDescription());
        assertEquals(request.getCreated(), requestDto.getCreated());
        assertEquals(1, requestDto.getItems().size());
    }

    @Test
    void toRequest_FromRequestDtoInput() {
        RequestDtoInput requestDtoInput = new RequestDtoInput("Looking for Balalaika");
        Request request = RequestMapper.INSTANCE.toRequest(requestDtoInput);

        assertNotNull(request);

        assertNull(request.getId());
        assertEquals(requestDtoInput.getDescription(), request.getDescription());
        assertNull(request.getRequestingUser());
        assertNotNull(request.getCreated());
    }

    @Test
    void testShouldReturnNull() {
        Request request = null;

        RequestDtoShortOutput requestDtoShortOutput = RequestMapper.INSTANCE.toRequestDtoShortOutput(request);
        RequestDtoOutput requestDtoOutput = RequestMapper.INSTANCE.toRequestDtoOutput(request);
        RequestDto requestDto = RequestMapper.INSTANCE.toRequestDto(request, null);
        Request requestIsNull = RequestMapper.INSTANCE.toRequest(null);

        assertNull(requestDtoShortOutput);
        assertNull(requestDtoOutput);
        assertNull(requestDto);
        assertNull(requestIsNull);
    }
}
