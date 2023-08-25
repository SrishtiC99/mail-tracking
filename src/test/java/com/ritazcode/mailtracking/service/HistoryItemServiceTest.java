package com.ritazcode.mailtracking.service;

import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.entity.*;
import com.ritazcode.mailtracking.exception.BadAttributeValueException;
import com.ritazcode.mailtracking.exception.NoSuchElementException;
import com.ritazcode.mailtracking.mapper.HistoryItemMapper;
import com.ritazcode.mailtracking.repository.HistoryItemRepository;
import com.ritazcode.mailtracking.repository.PostalItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryItemServiceTest extends BaseTest {

    @Mock
    private HistoryItemRepository repository;
    @Mock
    private HistoryItemMapper mapper;
    @Mock
    private PostalItemRepository postalItemRepository;

    @InjectMocks
    private HistoryItemService service;

    private PostalItem postalItem;
    private PostOffice office;
    private HistoryItem historyItem;

    @BeforeEach
    void setUp() {
        office = PostOffice.builder()
                .id(1L)
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();
        postalItem = PostalItem.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.REGISTERED)
                .postOffice(office)
                .build();
        historyItem = HistoryItem.builder()
                .id(1L)
                .status(ItemStatus.REGISTERED)
                .item(postalItem)
//                .timestamp(new Timestamp())
                .build();
        postalItem.setHistory(List.of(historyItem));
    }

    @Test
    @DisplayName("findByItemId() should return paged list of postal item history records")
    void findByItemId_ReturnPagedListOfItemHistory() {
        //arrange
        PostalItemHistoryDto expectedResultContent = PostalItemHistoryDto.builder()
                .build();
        when(postalItemRepository.findById(postalItem.getId())).thenReturn(Optional.of(postalItem));
        when(mapper.HistoryItem_To_PostalItemHistoryDto(historyItem)).thenReturn(expectedResultContent);


        Page<PostalItemHistoryDto> expectedResult = new PageImpl<>(List.of(expectedResultContent), mock(PageRequest.class), 0);
        Page<HistoryItem> dbPagedList = new PageImpl<>(List.of(historyItem), mock(PageRequest.class), 0);

        when(repository.findByItemId(postalItem.getId(), PageRequest.of(0,2))).thenReturn(dbPagedList);

        //act
        Page<PostalItemHistoryDto> actualResult = service.findByItemId(postalItem.getId(), PageRequest.of(0,2));
        //assert
        assertThat(actualResult.getContent()).isNotEmpty();
        assertThat(actualResult.getContent()).containsAll(expectedResult);
        assertThat(actualResult.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("findByItemId() should throw an exception when postal postalItem id is null")
    void findByItemId_ThrowException_PostalItemIdIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.findByItemId(null, mock(PageRequest.class))
        );
        assertThat(thrown.getMessage()).containsAnyOf("postal item id is not valid");
        verify(repository,never()).findByItemId(any(),any());
    }

    @Test
    @DisplayName("findByItemId() should throw an exception when postal item is not found")
    void findByItemId_ThrowException_PostalItemIsNotFound() {
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.findByItemId(postalItem.getId(), PageRequest.of(0, 2))
        );
        assertThat(thrown.getMessage()).containsAnyOf("postal item with id", "is not found");
        verify(repository,never()).findByItemId(any(),any());

    }


}