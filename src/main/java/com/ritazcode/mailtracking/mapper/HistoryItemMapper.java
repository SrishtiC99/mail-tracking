package com.ritazcode.mailtracking.mapper;

import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.entity.HistoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface HistoryItemMapper {

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "type", source = "item.type")
    @Mapping(target = "recipientName", source = "item.recipientName")
    @Mapping(target = "postOfficeId", source = "item.postOffice.id")
    @Mapping(target = "postOfficeName", source = "item.postOffice.name")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "timestamp", source = "timestamp")
    PostalItemHistoryDto HistoryItem_To_PostalItemHistoryDto(HistoryItem historyItem);
}
