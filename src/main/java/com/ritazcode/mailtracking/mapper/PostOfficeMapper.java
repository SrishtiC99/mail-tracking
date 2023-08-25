package com.ritazcode.mailtracking.mapper;

import com.ritazcode.mailtracking.dto.postOffice.CreatePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.ResponsePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.UpdatePostOfficeDto;
import com.ritazcode.mailtracking.entity.PostOffice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostOfficeMapper {
    @Mapping(target = "id", source="id")
    ResponsePostOfficeDto DbPostOffice_To_ResponsePostOfficeDto(PostOffice postOffice);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "postalItems", ignore = true)
    PostOffice UpdatePostOfficeDto_To_DbPostOffice(UpdatePostOfficeDto updatedPostDto, @MappingTarget PostOffice postOffice);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "postalItems", ignore = true)
    PostOffice CreatePostOfficeDto_To_DbPostOffice(CreatePostOfficeDto newDto);
}
