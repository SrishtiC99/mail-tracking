package com.ritazcode.mailtracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.dto.postalItem.RegisterItemDto;
import com.ritazcode.mailtracking.dto.postalItem.ResponseItemDto;
import com.ritazcode.mailtracking.dto.postalItem.UpdateItemDto;
import com.ritazcode.mailtracking.entity.ItemStatus;
import com.ritazcode.mailtracking.entity.ItemType;
import com.ritazcode.mailtracking.entity.PostOffice;
import com.ritazcode.mailtracking.entity.PostalItem;
import com.ritazcode.mailtracking.repository.PostalItemRepository;
import com.ritazcode.mailtracking.service.BaseTest;
import com.ritazcode.mailtracking.service.PostalItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostalItemControllerTest extends BaseTest {

    @MockBean
    PostalItemService service;

    @MockBean
    PostalItemRepository repository;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    MockMvc mockMvc;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    ObjectMapper objectMapper;

    private ResponseItemDto responseItemDto;
    private PostalItem postalItem;

    private PostOffice office;


    @BeforeEach
    void setUp() {
        responseItemDto = ResponseItemDto.builder()
                .id(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .status(ItemStatus.ARRIVED)
                .build();
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
    @DisplayName("getAllItems() should return paged list of postal items")
    void getAllItems_shouldReturnPagedListOfPostalItems() throws Exception {
        Page<ResponseItemDto> pagedResult = new PageImpl<>(List.of(responseItemDto), mock(PageRequest.class), 0);
        String expectedResult = objectMapper.writeValueAsString(pagedResult);
        when(service.getPostalItems(any(PageRequest.class))).thenReturn(pagedResult);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/postal-item/all-items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("view() should return postal item information as ResponseItemDto")
    void view_shouldReturnPostalItemInformation() throws Exception {
        String expectedResult = objectMapper.writeValueAsString(responseItemDto);

        when(service.view(postalItem.getId())).thenReturn(responseItemDto);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/postal-item/view/{id}", postalItem.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("showItemHistory() should return paged list of the history of postal item")
    void showItemHistory() throws Exception {
        PostalItemHistoryDto expectedResultContent = PostalItemHistoryDto.builder()
                .itemId(1L)
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .postOfficeId(1L)
                .postOfficeName("post office")
                .status(ItemStatus.ARRIVED)
                .timestamp(new Date())
                .build();

        Page<PostalItemHistoryDto> pagedResult = new PageImpl<>(List.of(expectedResultContent), mock(PageRequest.class), 0);
        String expectedResult = objectMapper.writeValueAsString(pagedResult);

        when(service.getPostalItemHistory(anyLong(), any(PageRequest.class))).thenReturn(pagedResult);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/postal-item/history")
                        .param("id", String.valueOf(postalItem.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("getOfficeItems() should return paged list of postal items that are in a specific post office")
    void getOfficeItems() throws Exception {
        Page<ResponseItemDto> pagedResult = new PageImpl<>(List.of(responseItemDto), mock(PageRequest.class), 0);
        String expectedResult = objectMapper.writeValueAsString(pagedResult);

        when(service.getOfficeItems(anyLong(), any(PageRequest.class))).thenReturn(pagedResult);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/postal-item/all-office-items")
                        .param("id", String.valueOf(office.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }


    @Test
    @DisplayName("register() should return the new registered postal item information as ResponseItemDto")
    void register_shouldReturnRegisteredItemInformation() throws Exception {

        RegisterItemDto content = RegisterItemDto.builder()
                .type(ItemType.LETTER)
                .recipientName("recipient name")
                .recipientAddress("recipient address")
                .recipientIndex("1235654")
                .build();
        String contentAsString = objectMapper.writeValueAsString(content);
        String expectedResult = objectMapper.writeValueAsString(responseItemDto);

        when(service.register(content)).thenReturn(responseItemDto);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/postal-item/create")
                        .content(contentAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("arrive() should return updated postal item with status ARRIVED")
    void arrive_shouldReturnUpdatedPostalItemInformation() throws Exception {
        UpdateItemDto content = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();

        String contentAsString = objectMapper.writeValueAsString(content);
        String expectedResult = objectMapper.writeValueAsString(responseItemDto);

        when(service.arriveToPostOffice(content)).thenReturn(responseItemDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/postal-item/arrive")
                        .content(contentAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andExpect(jsonPath("$.status", is(ItemStatus.ARRIVED.name())))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("arrive() should return updated postal item with status DEPART")
    void send_shouldReturnUpdatedPostalItemInformation() throws Exception {
        UpdateItemDto content = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();
        responseItemDto.setStatus(ItemStatus.DEPARTED);

        String contentAsString = objectMapper.writeValueAsString(content);
        String expectedResult = objectMapper.writeValueAsString(responseItemDto);

        when(service.departFromPostOffice(content)).thenReturn(responseItemDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/postal-item/depart")
                        .content(contentAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andExpect(jsonPath("$.status", is(ItemStatus.DEPARTED.name())))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("receive() should return updated postal item with status RECEIVED")
    void receive_shouldReturnUpdatedPostalItemInformation() throws Exception {
        UpdateItemDto content = UpdateItemDto.builder()
                .id(1L)
                .postOfficeId(1L)
                .build();
        responseItemDto.setStatus(ItemStatus.RECEIVED);

        String contentAsString = objectMapper.writeValueAsString(content);
        String expectedResult = objectMapper.writeValueAsString(responseItemDto);

        when(service.receiveByRecipient(content)).thenReturn(responseItemDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/postal-item/receive")
                        .content(contentAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andExpect(jsonPath("$.status", is(ItemStatus.RECEIVED.name())))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}