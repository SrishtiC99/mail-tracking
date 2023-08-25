package com.ritazcode.mailtracking.service;

import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.dto.postalItem.RegisterItemDto;
import com.ritazcode.mailtracking.dto.postalItem.ResponseItemDto;
import com.ritazcode.mailtracking.dto.postalItem.UpdateItemDto;
import com.ritazcode.mailtracking.entity.*;
import com.ritazcode.mailtracking.exception.BadAttributeValueException;
import com.ritazcode.mailtracking.exception.NoSuchElementException;
import com.ritazcode.mailtracking.mapper.PostalItemMapper;
import com.ritazcode.mailtracking.repository.PostOfficeRepository;
import com.ritazcode.mailtracking.repository.PostalItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostalItemServiceTest extends BaseTest {
    @Mock
    private PostalItemMapper itemMapper;
    @Mock
    private PostalItemRepository itemRepository;
    @Mock
    private PostOfficeRepository postOfficeRepository;
    @Mock
    private HistoryItemService historyItemService;

    @InjectMocks
    private PostalItemService service;

    private PostalItem postalItem;
    private PostOffice office;


    @BeforeEach
    void setUp() {
        //arrange
        office = PostOffice.builder()
                .id(1L)
                .name("post office")
                .address("street 1")
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
    }

    @Test
    @DisplayName("getPostalItems() should return paged list of postal items")
    void getPostalItems_ReturnPagedList() {
        //arrange
        ResponseItemDto expectedResultContent = ResponseItemDto.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.REGISTERED)
                .build();
        Page<ResponseItemDto> expectedResult = new PageImpl<>(List.of(expectedResultContent), mock(PageRequest.class), 0);
        Page<PostalItem> dbPagedList = new PageImpl<>(List.of(postalItem), mock(PageRequest.class), 0);

        when(itemMapper.PostalItem_To_ResponseItemDto(postalItem)).thenReturn(expectedResultContent);
        when(itemRepository.findAll(PageRequest.of(0, 2))).thenReturn(dbPagedList);
        //act
        Page<ResponseItemDto> actualResult = service.getPostalItems(PageRequest.of(0, 2));
        //assert
        assertThat(actualResult.getContent()).isNotEmpty();
        assertThat(actualResult.getContent()).containsAll(expectedResult);
        assertThat(actualResult.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("view() should return postal item information by passing item id")
    void view_ReturnPostalItemInfo() {
        //arrange
        ResponseItemDto expectedResult = ResponseItemDto.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.REGISTERED)
                .build();

        when(itemRepository.findById(postalItem.getId())).thenReturn(Optional.of(postalItem));
        when(itemMapper.PostalItem_To_ResponseItemDto(postalItem)).thenReturn(expectedResult);
        //act
        ResponseItemDto actualResult = service.view(postalItem.getId());
        //assert
        assertThat(actualResult).isNotNull();
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    @DisplayName("view() should throw an exception when postal item id is null")
    void view_ThrowException_WhenPostalItemIdIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.view(null)
        );
        assertThat(thrown.getMessage()).isEqualTo("item id is not valid");
        verify(itemRepository, never()).findById(anyLong());
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @Test
    @DisplayName("view() should throw an exception when postal item is not found")
    void view_ThrowException_WhenPostalItemIsNotFound() {
        //arrange
        when(itemRepository.findById(postalItem.getId())).thenReturn(Optional.empty());
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.view(postalItem.getId())
        );
        assertThat(thrown.getMessage()).containsAnyOf("item with id", "is not found");
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }


    @Test
    @DisplayName("getOfficeItems() should return paged list of postal items in a specific office")
    void getOfficeItems_ReturnPostalItemsOfOffice() {
        //arrange
        ResponseItemDto expectedResultContent = ResponseItemDto.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.REGISTERED)
                .build();
        Page<ResponseItemDto> expectedResult = new PageImpl<>(List.of(expectedResultContent), mock(PageRequest.class), 0);
        Page<PostalItem> dbPagedList = new PageImpl<>(List.of(postalItem), mock(PageRequest.class), 0);

        when(postOfficeRepository.findById(office.getId())).thenReturn(Optional.of(office));
        when(itemMapper.PostalItem_To_ResponseItemDto(postalItem)).thenReturn(expectedResultContent);
        when(itemRepository.findAllByPostOfficeId(office.getId(), PageRequest.of(0, 2))).thenReturn(dbPagedList);
        //act
        Page<ResponseItemDto> actualResult = service.getOfficeItems(office.getId(), PageRequest.of(0, 2));
        //assert
        assertThat(actualResult.getContent()).isNotEmpty();
        assertThat(actualResult.getContent()).containsAll(expectedResult);
        assertThat(actualResult.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("getOfficeItems() should throw an exception when office id is null")
    void getOfficeItems_ThrowException_WhenPostOfficeIdIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.getOfficeItems(null, mock(PageRequest.class))
        );
        assertThat(thrown.getMessage()).isEqualTo("post office id is not valid");

        verify(postOfficeRepository, never()).findById(anyLong());
        //    verify(itemRepository,never()).findAllByPostOfficeId(anyLong(),mock(PageRequest.class));
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @Test
    @DisplayName("getOfficeItems() should throw an exception when office is not found")
    void getOfficeItems_ThrowException_WhenPostOfficeIsNotFound() {
        //arrange
        when(postOfficeRepository.findById(office.getId())).thenReturn(Optional.empty());
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.getOfficeItems(office.getId(), PageRequest.of(0, 2))
        );
        assertThat(thrown.getMessage()).containsAnyOf("post office  with id ", "is not found");
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @Test
    @DisplayName("register() should return registered postal item information")
    void register_ReturnRegisteredItem() {
        //arrange
        postalItem.getHistory().add(
                new HistoryItem(ItemStatus.REGISTERED, postalItem)
        );

        RegisterItemDto createdItemDto = RegisterItemDto.builder()
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .build();
        ResponseItemDto expectedResult = ResponseItemDto.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.REGISTERED)
                .build();


        when(itemMapper.RegisterItemDto_to_PostalItem(createdItemDto, ItemStatus.REGISTERED)).thenReturn(postalItem);
        when(itemRepository.save(postalItem)).thenReturn(postalItem);
        when(itemMapper.PostalItem_To_ResponseItemDto(postalItem)).thenReturn(expectedResult);
        //act
        ResponseItemDto actualResult = service.register(createdItemDto);
        //assert
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("register() should throw an exception when created postal item is null")
    void register_ThrowException_WhenCreatedPostalItemIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.register(null)
        );
        assertThat(thrown.getMessage()).containsAnyOf("the passed value of postal item is not valid");
        verify(itemRepository, never()).save(any(PostalItem.class));
        verify(itemMapper, never()).RegisterItemDto_to_PostalItem(any(), any());
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @Test
    @DisplayName("arriveToPostOffice() should return the postal item with its status updated to ARRIVED")
    void arriveToPostOffice_ReturnPostalItemWithUpdatedStatus() {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();

        ResponseItemDto expectedResult = ResponseItemDto.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.ARRIVED)
                .build();
        when(itemRepository.findById(postalItem.getId())).thenReturn(Optional.of(postalItem));
        when(postOfficeRepository.findById(office.getId())).thenReturn(Optional.of(office));
        when(service.updateItem(updatedItem, any(ItemStatus.class))).thenReturn(expectedResult);
        //act
        ResponseItemDto actualResult = service.arriveToPostOffice(updatedItem);
        //assert
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).usingRecursiveComparison().isEqualTo(expectedResult);
        assertThat(actualResult.getStatus()).isEqualTo(ItemStatus.ARRIVED);
    }

    @Test
    @DisplayName("departFromPostOffice() should return the postal item with its status updated to DEPARTED")
    void departFromPostOffice_ReturnPostalItemWithUpdatedStatus() {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();

        ResponseItemDto expectedResult = ResponseItemDto.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.DEPARTED)
                .build();
        when(itemRepository.findById(postalItem.getId())).thenReturn(Optional.of(postalItem));
        when(postOfficeRepository.findById(office.getId())).thenReturn(Optional.of(office));
        when(service.updateItem(updatedItem, any(ItemStatus.class))).thenReturn(expectedResult);
        //act
        ResponseItemDto actualResult = service.departFromPostOffice(updatedItem);
        //assert
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).usingRecursiveComparison().isEqualTo(expectedResult);
        assertThat(actualResult.getStatus()).isEqualTo(ItemStatus.DEPARTED);
    }

    @Test
    @DisplayName("receiveByRecipient() should return the postal item with its status updated to RECEIVED")
    void receiveByRecipient_ReturnPostalItemWithUpdatedStatus() {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();

        ResponseItemDto expectedResult = ResponseItemDto.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.RECEIVED)
                .build();
        when(itemRepository.findById(postalItem.getId())).thenReturn(Optional.of(postalItem));
        when(postOfficeRepository.findById(office.getId())).thenReturn(Optional.of(office));
        when(service.updateItem(updatedItem, any(ItemStatus.class))).thenReturn(expectedResult);
        //act
        ResponseItemDto actualResult = service.receiveByRecipient(updatedItem);
        //assert
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).usingRecursiveComparison().isEqualTo(expectedResult);
        assertThat(actualResult.getStatus()).isEqualTo(ItemStatus.RECEIVED);
    }

    @ParameterizedTest
    @EnumSource(ItemStatus.class)
    @DisplayName("updateItem() should throw an exception when postal item id is null")
    void updateItem_ThrowException_WhenPostalItemIdIsNull(ItemStatus status) {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .postOfficeId(1L)
                .build();
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.updateItem(updatedItem, status)
        );
        assertThat(thrown.getMessage()).isEqualTo("item id is not valid");

        verify(itemRepository, never()).save(any(PostalItem.class));
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @ParameterizedTest
    @EnumSource(ItemStatus.class)
    @DisplayName("updateItem() should throw an exception when post office id is null")
    void updateItem_ThrowException_WhenPostOfficeIdIsNull(ItemStatus status) {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .build();
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.updateItem(updatedItem, status)
        );
        assertThat(thrown.getMessage()).isEqualTo("post office id is not valid");

        verify(itemRepository, never()).save(any(PostalItem.class));
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @ParameterizedTest
    @EnumSource(ItemStatus.class)
    @DisplayName("updateItem() should throw an exception when postal item is not found")
    void updateItem_ThrowException_WhenPostalItemIsNotFound(ItemStatus status) {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();
        when(itemRepository.findById(updatedItem.getId())).thenReturn(Optional.empty());
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.updateItem(updatedItem, status)
        );
        assertThat(thrown.getMessage()).isEqualTo("item with id (" + updatedItem.getId() + ") is not found");

        verify(itemRepository, never()).save(any(PostalItem.class));
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @ParameterizedTest
    @EnumSource(ItemStatus.class)
    @DisplayName("updateItem() should throw an exception when post office is not found")
    void updateItem_ThrowException_WhenPostOfficeIsNotFound(ItemStatus status) {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();
        when(itemRepository.findById(updatedItem.getId())).thenReturn(Optional.of(postalItem));
        when(postOfficeRepository.findById(updatedItem.getPostOfficeId())).thenReturn(Optional.empty());
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.updateItem(updatedItem, status)
        );
        assertThat(thrown.getMessage()).isEqualTo("post office  with id (" + updatedItem.getId() + ") is not found");

        verify(itemRepository, never()).save(any(PostalItem.class));
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @ParameterizedTest
    @EnumSource(ItemStatus.class)
    @DisplayName("updateItem() should throw an exception when postal item is already received")
    void updateItem_ThrowException_WhenPostalItemIsAlreadyReceived(ItemStatus status) {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();

       postalItem.setStatus(ItemStatus.RECEIVED);
        when(itemRepository.findById(updatedItem.getId())).thenReturn(Optional.of(postalItem));

//        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.updateItem(updatedItem, status)
        );
        assertThat(thrown.getMessage()).isEqualTo("item with id (" + updatedItem.getId() + ") has already been RECEIVED");

        verify(itemRepository, never()).save(any(PostalItem.class));
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }
    @Test
    @DisplayName("updateItem() should throw an exception when postal item is willing to depart from a different post office from the one it received it")
    void updateItem_ThrowException_WhenPostalItemDidNotArriveToTheSameOffice() {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(2L)
                .build();
        PostOffice office2 = PostOffice.builder()
        .id(2L)
        .name("post office 2")
        .address("street 2")
        .index("2525258974")
        .build();
        postalItem.setPostOffice(office);
        when(itemRepository.findById(updatedItem.getId())).thenReturn(Optional.of(postalItem));
        when(postOfficeRepository.findById(updatedItem.getPostOfficeId())).thenReturn(Optional.of(office2));

    //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.updateItem(updatedItem, ItemStatus.DEPARTED)
        );
        assertThat(thrown.getMessage()).isEqualTo("item with id (" + updatedItem.getId() + ") can not DEPART from a post office ("+office2.getId()+") to which it did NOT ARRIVE");

        verify(itemRepository, never()).save(any(PostalItem.class));
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }

    @Test
    @DisplayName("updateItem() should throw an exception when postal item is willing to depart while it did not arrive to any office")
    void updateItem_ThrowException_WhenPostalItemDidNotArriveToAnyOffice() {
        //arrange
        UpdateItemDto updatedItem = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();

        postalItem.setPostOffice(null);
        when(itemRepository.findById(updatedItem.getId())).thenReturn(Optional.of(postalItem));
        when(postOfficeRepository.findById(updatedItem.getPostOfficeId())).thenReturn(Optional.of(office));

        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.updateItem(updatedItem, ItemStatus.DEPARTED)
        );
        assertThat(thrown.getMessage()).isEqualTo("item with id (" + updatedItem.getId() + ") did NOT ARRIVE to post office yet");

        verify(itemRepository, never()).save(any(PostalItem.class));
        verify(itemMapper, never()).PostalItem_To_ResponseItemDto(any(PostalItem.class));
    }
    @Test
    @DisplayName("getPostalItemHistory() should return paged list of a postal item history")
    void getPostalItemHistory_ReturnPagedListPostalItemHistory() {

        PostalItemHistoryDto expectedResultContent = PostalItemHistoryDto.builder()
                .build();
        Page<PostalItemHistoryDto> expectedResult = new PageImpl<>(List.of(expectedResultContent), mock(PageRequest.class), 0);
        when(itemRepository.existsById(postalItem.getId())).thenReturn(true);
        when(historyItemService.findByItemId(postalItem.getId(), PageRequest.of(0, 2))).thenReturn(expectedResult);
        //act
        Page<PostalItemHistoryDto> actualResult = service.getPostalItemHistory(postalItem.getId(), PageRequest.of(0, 2));
        //assert
        assertThat(actualResult.getContent()).isNotEmpty();
        assertThat(actualResult.getContent()).containsAll(expectedResult);
        assertThat(actualResult.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("getPostalItemHistory() should throw an exception when postal item is not found")
    void getPostalItemHistory_ThrowException_WhenPostalItemIsNotFound() {
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.getPostalItemHistory(postalItem.getId(), PageRequest.of(0, 2))
        );
        assertThat(thrown.getMessage()).isEqualTo("item with id (" + postalItem.getId() + ") is not found");
    }

    @Test
    @DisplayName("getPostalItemHistory() should throw an exception when postal item id is null")
    void getPostalItemHistory_ThrowException_WhenPostalItemIdIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.getPostalItemHistory(null, PageRequest.of(0, 2))
        );
        assertThat(thrown.getMessage()).containsAnyOf("item id is not valid");
    }
}