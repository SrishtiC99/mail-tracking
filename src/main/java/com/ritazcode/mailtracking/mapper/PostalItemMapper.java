package com.ritazcode.mailtracking.mapper;

import com.ritazcode.mailtracking.dto.postalItem.RegisterItemDto;
import com.ritazcode.mailtracking.dto.postalItem.ResponseItemDto;
import com.ritazcode.mailtracking.entity.ItemStatus;
import com.ritazcode.mailtracking.entity.PostalItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostalItemMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "postOffice", ignore = true)
    @Mapping(target = "history", ignore = true)
    PostalItem RegisterItemDto_to_PostalItem(RegisterItemDto dto, ItemStatus status);

    ResponseItemDto PostalItem_To_ResponseItemDto(PostalItem dbItem);
}
