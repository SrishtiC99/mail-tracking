package com.ritazcode.mailtracking.mapper;

import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.entity.*;
import com.ritazcode.mailtracking.service.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HistoryItemMapperTest extends BaseTest {


    @Spy
    private HistoryItemMapper mapper = Mappers.getMapper(HistoryItemMapper.class);

    HistoryItem historyItem;
    PostalItemHistoryDto postalItemHistoryDto;
    PostalItem postalItem;
    PostOffice office;

    @BeforeEach
    void SetUp() {
        postalItemHistoryDto = PostalItemHistoryDto.builder()
                .itemId(1L)
                .recipientName("name")
                .postOfficeId(1L)
                .postOfficeName("office name")
                .status(ItemStatus.REGISTERED)
                .timestamp(new Date(System.currentTimeMillis()))
                .build();

        office= PostOffice.builder()
                .id(1L)
                .name("office name")
                .build();
        postalItem= PostalItem.builder()
                .id(1L)
                .recipientName("name")
                .status(ItemStatus.REGISTERED)
                .type(ItemType.POSTCARD)
                .postOffice(office)
                .build();
        historyItem = HistoryItem.builder()
                .status(ItemStatus.REGISTERED)
                .item(postalItem)
                .build();
    }

    @Test
    void historyItem_To_PostalItemHistoryDto() {

        PostalItemHistoryDto expectedResult = mapper.HistoryItem_To_PostalItemHistoryDto(historyItem);

        assertThat(expectedResult.getItemId()).isEqualTo(postalItem.getId());
        assertThat(expectedResult.getRecipientName()).isEqualTo(postalItem.getRecipientName());
        assertThat(expectedResult.getPostOfficeId()).isEqualTo(office.getId());
        assertThat(expectedResult.getPostOfficeName()).isEqualTo(office.getName());
        assertThat(expectedResult.getType()).isSameAs(postalItem.getType());
    }
}