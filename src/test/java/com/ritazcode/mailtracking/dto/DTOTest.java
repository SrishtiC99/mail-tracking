package com.ritazcode.mailtracking.dto;

import com.ritazcode.mailtracking.dto.postOffice.CreatePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.ResponsePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.UpdatePostOfficeDto;
import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.dto.postalItem.RegisterItemDto;
import com.ritazcode.mailtracking.dto.postalItem.ResponseItemDto;
import com.ritazcode.mailtracking.dto.postalItem.UpdateItemDto;
import com.ritazcode.mailtracking.exception.ErrorResponse;
import com.ritazcode.mailtracking.service.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.meanbean.test.BeanTester;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DTOTest extends BaseTest {

    @Test
    @DisplayName("should test all DTO getters and setters")
    void testAllDTOs() {
        BeanTester bean = new BeanTester();

        bean.testBean(PostalItemHistoryDto.class);
        bean.testBean(RegisterItemDto.class);
        bean.testBean(ResponseItemDto.class);
        bean.testBean(UpdateItemDto.class);

        bean.testBean(CreatePostOfficeDto.class);
        bean.testBean(ResponsePostOfficeDto.class);
        bean.testBean(UpdatePostOfficeDto.class);
    }
}