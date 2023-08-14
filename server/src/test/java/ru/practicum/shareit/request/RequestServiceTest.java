package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.exceptions.DtoIntegrityException;
import ru.practicum.shareit.item.dto.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {"db.name=test"})
public class RequestServiceTest {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private RequestMapperImpl requestMapper;

    @Spy
    private ItemMapperImpl itemMapper;

    private Request request;
    private User user;
    private Item item;

    @BeforeEach
    void setup() {
        request = new Request(1, "Looking for Balalaika", LocalDateTime.now(), user);
        item = new Item(1, "Balalaika", "Brand new balalaika", true, user, request);
        user = new User(2, "Shaun", "shaun@ya.ru");
    }

    @Test
    void add_shouldReturnRequestDtoShortOutput() {
        Integer userId = user.getId();
        RequestDtoInput requestDtoInput = new RequestDtoInput(request.getDescription());

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.ofNullable(user));

        Mockito.lenient()
                .when(requestRepository.save(any()))
                .thenReturn(request);

        RequestDtoShortOutput requestDtoSaved = requestService.add(requestDtoInput, userId);

        assertAll(
                () -> assertEquals(request.getDescription(), requestDtoSaved.getDescription()),
                () -> assertNotNull(requestDtoSaved.getCreated())
        );
    }

    @Test
    void add_throwsDtoIntegrityException() {
        Integer userId = user.getId();
        RequestDtoInput requestDtoInput = new RequestDtoInput();

        assertThrows(DtoIntegrityException.class, () -> requestService.add(requestDtoInput, userId));
    }

    @Test
    void getByUser_shouldReturnRequestDtoShortOutputWithItems() {
        Integer userId = user.getId();

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        Mockito.when(requestRepository.findByRequestingUserIdOrderByCreatedDesc(userId))
                .thenReturn(List.of(request));

        Mockito.when(itemRepository.findByRequest_IdIn(any()))
                .thenReturn(List.of(item));

        List<RequestDto> requestDtoSaved = requestService.getByUser(userId);

        assertNotNull(requestDtoSaved.get(0));
        assertEquals(requestDtoSaved.get(0).getItems().size(), 1);
    }

    @Test
    void getAll_shouldReturnRequestDtoOutput() {
        Integer from = 0;
        Integer size = 1;
        Integer userId = user.getId();

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        Mockito.when(requestRepository.findByRequestingUserIdNotOrderByCreatedDesc(userId))
                .thenReturn(List.of(request));

        Mockito.when(itemRepository.findByRequest_IdIn(List.of(request.getId())))
                .thenReturn(List.of(item));

        List<RequestDto> requestDtoSaved = requestService.getAll(from, size, userId);

        assertNotNull(requestDtoSaved.get(0));
        assertEquals(1, requestDtoSaved.get(0).getItems().size());
    }

    @Test
    void getAll_throwsRuntimeException() {
        Integer from = -1;
        Integer size = 0;
        Integer userId = user.getId();

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        assertThrows(RuntimeException.class, () -> requestService.getAll(from, size, userId));
    }

    @Test
    void get_shouldReturnRequestDtoOutput() {
        Integer userId = user.getId();
        Integer requestId = request.getId();

        Mockito.when(userRepository.existsById(userId))
                .thenReturn(true);

        Mockito.lenient()
                .when(requestRepository.findById(requestId))
                .thenReturn(Optional.ofNullable(request));

        Mockito.when(itemRepository.findByRequestId(requestId))
                .thenReturn(List.of(item));

        RequestDtoOutput requestDtoSaved = requestService.get(requestId, userId);

        assertNotNull(requestDtoSaved);
        assertEquals(requestDtoSaved.getItems().size(), 1);
    }
}
