package com.ritazcode.mailtracking.service;

import com.ritazcode.mailtracking.dto.postOffice.CreatePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.ResponsePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.UpdatePostOfficeDto;
import com.ritazcode.mailtracking.entity.PostOffice;
import com.ritazcode.mailtracking.exception.BadAttributeValueException;
import com.ritazcode.mailtracking.exception.NoSuchElementException;
import com.ritazcode.mailtracking.mapper.PostOfficeMapper;
import com.ritazcode.mailtracking.repository.PostOfficeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostOfficeServiceTest extends BaseTest {

    @Mock
    private PostOfficeRepository repository;
    @Mock
    private PostOfficeMapper mapper;

    @InjectMocks
    private PostOfficeService service;

    private PostOffice postOffice;

    @BeforeEach
    void setUp() {
        //arrange
        postOffice = PostOffice.builder()
                .id(1L)
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();
    }

    @Test
    @DisplayName("view() should return post office item information by passing office id")
    void view_ReturnPostOfficeInfo() {
        //arrange
        ResponsePostOfficeDto expectedResult = ResponsePostOfficeDto.builder()
                .id(1L)
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();
        when(repository.findById(postOffice.getId())).thenReturn(Optional.of(postOffice));
        when(mapper.DbPostOffice_To_ResponsePostOfficeDto(postOffice)).thenReturn(expectedResult);
        //act
        ResponsePostOfficeDto actualResult = service.view(postOffice.getId());
        //assert
        assertThat(actualResult).isNotNull();
        assertThat(expectedResult).isEqualTo(actualResult);
    }

    @Test
    @DisplayName("view() should throw an exception when post office id is null")
    void view_ThrowException_PostOfficeIdIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.view(null)
        );
        assertThat(thrown.getMessage()).isEqualTo("post office id is not valid");
        verify(repository, never()).findById(any());
        verify(mapper, never()).DbPostOffice_To_ResponsePostOfficeDto(any());
    }

    @Test
    @DisplayName("view() should throw an exception when post office is not found")
    void view_ThrowException_PostOfficeIsNotFound() {
        //arrange
        when(repository.findById(postOffice.getId())).thenReturn(Optional.empty());
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.view(postOffice.getId())
        );
        assertThat(thrown.getMessage()).contains("No such post office with id");
        verify(mapper, never()).DbPostOffice_To_ResponsePostOfficeDto(any());
    }

    @Test
    @DisplayName("update() should return updated  post office information")
    void update_ReturnUpdatedOfficeInformation() {
        //arrange
        UpdatePostOfficeDto updatedOffice = UpdatePostOfficeDto.builder()
                .id(1L)
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();
        ResponsePostOfficeDto expectedResult = ResponsePostOfficeDto.builder()
                .id(1L)
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();

        when(repository.findById(postOffice.getId())).thenReturn(Optional.of(postOffice));
        when(mapper.UpdatePostOfficeDto_To_DbPostOffice(updatedOffice, postOffice)).thenReturn(postOffice);
        when(repository.save(postOffice)).thenReturn(postOffice);
        when(mapper.DbPostOffice_To_ResponsePostOfficeDto(postOffice)).thenReturn(expectedResult);
        //act
        ResponsePostOfficeDto actualResult = service.update(updatedOffice);
        //assert
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("update()  should throw an exception when updated post office id is null")
    void update_ThrowException_UpdatedItemIdIsNull() {
        //arrange
        UpdatePostOfficeDto updatedOffice = UpdatePostOfficeDto.builder()
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.update(updatedOffice)
        );
        assertThat(thrown.getMessage()).containsAnyOf("post office id is not valid");
        verify(repository, never()).findById(any());
        verify(mapper, never()).UpdatePostOfficeDto_To_DbPostOffice(any(), any());
        verify(repository, never()).save(any());
        verify(mapper, never()).DbPostOffice_To_ResponsePostOfficeDto(any());
    }

    @Test
    @DisplayName("update()  should throw an exception when updated post office is null")
    void update_ThrowException_UpdatedOfficeIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.update(null)
        );
        assertThat(thrown.getMessage()).containsAnyOf("the passed post office values");
        verify(repository, never()).findById(any());
        verify(mapper, never()).UpdatePostOfficeDto_To_DbPostOffice(any(), any());
        verify(repository, never()).save(any());
        verify(mapper, never()).DbPostOffice_To_ResponsePostOfficeDto(any());
    }

    @Test
    @DisplayName("update() should throw an exception when post office is not found")
    void update_ThrowException_PostOfficeIsNotFound() {
        //arrange
        UpdatePostOfficeDto updatedOffice = UpdatePostOfficeDto.builder()
                .id(1L)
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();
        when(repository.findById(updatedOffice.getId())).thenReturn(Optional.empty());
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.update(updatedOffice)
        );
        assertThat(thrown.getMessage()).containsAnyOf("No such post office with id");
        verify(mapper, never()).UpdatePostOfficeDto_To_DbPostOffice(any(), any());
        verify(repository, never()).save(any(PostOffice.class));
        verify(mapper, never()).DbPostOffice_To_ResponsePostOfficeDto(any());
    }


    @Test
    @DisplayName("create()  should return created post office information")
    void create_ReturnCreatedOffice() {
        //arrange
        CreatePostOfficeDto createdOfficeDto = CreatePostOfficeDto.builder()
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();
        ResponsePostOfficeDto expectedResult = ResponsePostOfficeDto.builder()
                .name("post office")
                .address("street 2")
                .index("1232322")
                .build();

        when(mapper.CreatePostOfficeDto_To_DbPostOffice(createdOfficeDto)).thenReturn(postOffice);
        when(repository.save(postOffice)).thenReturn(postOffice);
        when(mapper.DbPostOffice_To_ResponsePostOfficeDto(postOffice)).thenReturn(expectedResult);
        //act
        ResponsePostOfficeDto actualResult = service.create(createdOfficeDto);
        //assert
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("create()  should throw an exception when created post office is null")
    void create_ThrowException_CreatedOfficeIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.create(null)
        );
        assertThat(thrown.getMessage()).containsAnyOf("the passed value of post office");
        verify(mapper, never()).CreatePostOfficeDto_To_DbPostOffice(any());
        verify(repository, never()).save(any(PostOffice.class));
        verify(mapper, never()).DbPostOffice_To_ResponsePostOfficeDto(any());
    }

    @Test
    @DisplayName("should delete a specific post office")
    void delete_PostOfficeIsDeleted() {
        //arrange
        when(repository.findById(postOffice.getId())).thenReturn(Optional.of(postOffice));
        //act
        service.delete(postOffice.getId());
        //assert
        verify(repository, times(1)).deleteById(postOffice.getId());
    }


    @Test
    @DisplayName("delete() should throw an exception when post office id is null")
    void delete_ThrowException_PostOfficeIdIsNull() {
        //act
        //assert
        BadAttributeValueException thrown = assertThrows(
                BadAttributeValueException.class, () -> service.delete(null)
        );
        assertThat(thrown.getMessage()).containsAnyOf("is not valid");
        verify(repository,never()).findById(anyLong());
        verify(repository,never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("delete() should throw an exception when post office is not found")
    void delete_ThrowException_PostOfficeIsNotFound() {
        //arrange
        when(repository.findById(postOffice.getId())).thenReturn(Optional.empty());
        //act
        //assert
        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class, () -> service.delete(postOffice.getId())
        );
        assertThat(thrown.getMessage()).containsAnyOf("No such post office with id");
        verify(repository,never()).deleteById(anyLong());
    }
}