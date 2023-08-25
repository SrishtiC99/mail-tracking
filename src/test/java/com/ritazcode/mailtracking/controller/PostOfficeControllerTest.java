package com.ritazcode.mailtracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ritazcode.mailtracking.dto.postOffice.CreatePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.ResponsePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.UpdatePostOfficeDto;
import com.ritazcode.mailtracking.entity.PostOffice;
import com.ritazcode.mailtracking.service.BaseTest;
import com.ritazcode.mailtracking.service.PostOfficeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostOfficeControllerTest extends BaseTest {

    @MockBean
    private PostOfficeService postOfficeService;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MockMvc mockMvc;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ObjectMapper objectMapper;

    private ResponsePostOfficeDto responseOfficeDto;
    private PostOffice office;


    @BeforeEach
    void setUp() {
        responseOfficeDto = ResponsePostOfficeDto.builder()
                .id(1L)
                .name("post office")
                .address("street 123")
                .index("1232322")
                .build();

        office = PostOffice.builder()
                .id(1L)
                .name("post office")
                .address("street 123")
                .index("1232322")
                .build();
    }

    @Test
    @DisplayName("view() should return post office information as ResponsePostOfficeDto")
    void view_shouldReturnPostOfficeInformation() throws Exception {
        String expectedResult = objectMapper.writeValueAsString(responseOfficeDto);

        when(postOfficeService.view(office.getId())).thenReturn(responseOfficeDto);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/post-office/")
                        .param("id", String.valueOf(office.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
//                .andExpect(jsonPath("$.id", Matchers.is(responseOfficeDto.getId())))
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("create() should create a new post office and return its information as ResponsePostOfficeDto")
    void create_ShouldCreateNewPostOffice() throws Exception {

        String expectedResult = objectMapper.writeValueAsString(responseOfficeDto);

        CreatePostOfficeDto content = CreatePostOfficeDto.builder()
                .name("post office")
                .address("street 123")
                .index("1232322")
                .build();
        String contentAsString = new ObjectMapper()
                .writeValueAsString(content);

        when(postOfficeService.create(content)).thenReturn(responseOfficeDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/post-office/create")
                        .content(contentAsString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResult))
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        String actualResult = mvcResult.getResponse().getContentAsString();
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("update() should update an existing post office and return its information as ResponsePostOfficeDto")
    void update_ShouldUpdateAnExistingPostOfficeInformation() throws Exception {
        String expectedResult = objectMapper.writeValueAsString(responseOfficeDto);

        UpdatePostOfficeDto content = UpdatePostOfficeDto.builder()
                .id(1L)
                .name("post office")
                .address("street 123")
                .index("1232322")
                .build();
        String contentAsString = new ObjectMapper()
                .writeValueAsString(content);

        when(postOfficeService.update(content)).thenReturn(responseOfficeDto);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/post-office/update")
                        .content(contentAsString)
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
    @DisplayName("delete() should delete an existing post office")
    void delete_shouldDeleteAnExistingPostOffice() throws Exception {
        doNothing().when(postOfficeService).delete(office.getId());

        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/post-office/delete")
                        .param("id", String.valueOf(responseOfficeDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}