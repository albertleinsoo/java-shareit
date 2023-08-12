package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DtoIntegrityException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    public RequestDtoShortOutput add(RequestDtoInput requestDtoInput, Integer userId) {

        validateItemRequestDtoInput(requestDtoInput);

        Request request = requestMapper.toRequest(requestDtoInput);

        request.setRequestingUser(userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("Failed to process request. User with id = " + userId + " doesn't exist.")));

        request = requestRepository.save(request);

        return requestMapper.toRequestDtoShortOutput(request);
    }

    @Override
    public List<RequestDto> getByUser(Integer userId) {
        validateUser(userId);
        List<Request> requests = requestRepository.findByRequestingUserIdOrderByCreatedDesc(userId);

        return getItemRequestsDtoWithItems(requests);
    }

    @Override
    public List<RequestDto> getAll(Integer from, Integer size, Integer userId) {
        validateUser(userId);
        if (from != null && size != null) {
            validatePaginationParams(from, size);
        }

        List<Request> requests = requestRepository.findByRequestingUserIdNotOrderByCreatedDesc(userId);

        return getItemRequestsDtoWithItems(requests);
    }

    @Override
    public RequestDtoOutput get(Integer requestId, Integer userId) {
        validateUser(userId);

        Request request = requestRepository.findById(requestId).orElseThrow(() -> new ObjectNotFoundException("Failed to process request. User with id = " + requestId + " doesn't exist."));
        RequestDtoOutput itemRequestOutput = requestMapper.toRequestDtoOutput(request);

        itemRequestOutput.setItems(findItemsByRequestId(requestId));

        return itemRequestOutput;
    }

    private List<ItemDtoWithRequestId> findItemsByRequestId(Integer requestId) {
        List<Item> items = itemRepository.findByRequestId(requestId);
        return items.stream()
                .map(itemMapper::toItemDto)
                .map(itemDto -> new ItemDtoWithRequestId(itemDto, requestId))
                .collect(Collectors.toList());
    }

    private List<RequestDto> getItemRequestsDtoWithItems(List<Request> requests) {
        List<Integer> requestsId = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toList());

        List<Item> items = itemRepository.findByRequest_IdIn(requestsId);

        Map<Request, List<Item>> itemRequestsByItem = items.stream()
                .collect(groupingBy(Item::getRequest, toList()));

        List<RequestDto> requestDto = new ArrayList<>();

        for (Request itemRequest : requests) {
            List<Item> itemsTemp = itemRequestsByItem.getOrDefault(itemRequest, List.of());

            List<ItemDtoWithRequestId> itemDtoForRequests = itemsTemp.stream()
                    .map(itemMapper::toItemDtoWithRequestId)
                    .collect(toList());
            requestDto.add(requestMapper.toRequestDto(itemRequest, itemDtoForRequests));
        }

        return requestDto;
    }

    private void validatePaginationParams(Integer from, Integer size) {
        if (size <= 0 || from < 0) {
            throw new RuntimeException("Incorrect 'from' and 'size' pagination parameter values.");
        }
    }

    private void validateItemRequestDtoInput(RequestDtoInput requestDtoInput) {
        if (requestDtoInput.getDescription() == null || requestDtoInput.getDescription().isEmpty()) {
            throw new DtoIntegrityException("Failed to process request. Item request description must not be null or empty.");
        }
    }

    private void validateUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("Failed to process request. User with id = " + userId + " doesn't exist.");
        }
    }
}
