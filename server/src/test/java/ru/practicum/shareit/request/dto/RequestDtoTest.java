package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoTest {

    @Autowired
    private JacksonTester<RequestDto> jacksonTester;

    @Spy
    private  RequestMapperImpl requestMapper;

    @Test
    void testSerialize() throws Exception {
        User user = new User(2, "Shaun", "shaun@ya.ru");
        Request request = new Request(1, "Looking for Balalaika", LocalDateTime.now(), user);

        ItemDto itemDto = new ItemDto(1, "Balalaika", "Brand new balalaika", true);
        ItemDtoWithRequestId item = new ItemDtoWithRequestId(itemDto, 1);
        List<ItemDtoWithRequestId> items = new ArrayList<>(List.of(item));

        RequestDto requestDto = requestMapper.toRequestDto(request, items);

        JsonContent<RequestDto> requestDtoSaved = jacksonTester.write(requestDto);

        assertThat(requestDtoSaved).hasJsonPath("$.id");
        assertThat(requestDtoSaved).hasJsonPath("$.description");
        assertThat(requestDtoSaved).hasJsonPath("$.created");
        assertThat(requestDtoSaved).hasJsonPath("$.items");

        assertThat(requestDtoSaved).hasJsonPathValue("$.created");

        assertThat(requestDtoSaved).extractingJsonPathNumberValue("$.id").isEqualTo(request.getId());
        assertThat(requestDtoSaved).extractingJsonPathStringValue("$.description").isEqualTo(request.getDescription());
        assertThat(requestDtoSaved).extractingJsonPathArrayValue("$.items").hasSize(1);
    }
}
