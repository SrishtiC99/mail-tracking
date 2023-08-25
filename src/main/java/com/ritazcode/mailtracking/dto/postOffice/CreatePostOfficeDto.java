package com.ritazcode.mailtracking.dto.postOffice;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO object for creating a new post office")
public class CreatePostOfficeDto {

    @NotNull(message = "post office index is required")
    @Schema(description = "index of post office")
    String index;

    @NotNull(message = "post office name is required")
    @Schema(description = "name of post office")
    @Size(min = 3, max = 30, message = "post office name length should be at least 3 and at most 30")
    String name;

    @NotNull(message = "post office address is required")
    @Schema(description = "address of post office")
    @Size(min = 10, max = 100, message = "post office address length should be at least 10 and at most 100")
    String address;
}
