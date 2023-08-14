package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class RequestDto {
    private Integer id;

    private String description;

    private Integer requesterId;

    private LocalDateTime created;

    private List<ItemDtoWithRequestId> items;
}
