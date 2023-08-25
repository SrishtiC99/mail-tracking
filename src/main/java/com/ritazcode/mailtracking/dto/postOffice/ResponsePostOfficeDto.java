package com.ritazcode.mailtracking.dto.postOffice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Post office as response object")
public class ResponsePostOfficeDto {

    @Schema(description = "id of post office")
    Long id;

    @Schema(description = "index of post office")
    String index;

    @Schema(description = "name of post office")
    String name;

    @Schema(description = "address of post office")
    String address;
}
