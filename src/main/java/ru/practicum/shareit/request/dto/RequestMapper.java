package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDtoWithRequestId;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RequestMapper {
    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    RequestDtoShortOutput toRequestDtoShortOutput(Request request);

    RequestDtoOutput toRequestDtoOutput(Request request);

    RequestDto toRequestDto(Request itemRequest, List<ItemDtoWithRequestId> items);

    Request toRequest(RequestDtoInput requestDtoInput);
}
