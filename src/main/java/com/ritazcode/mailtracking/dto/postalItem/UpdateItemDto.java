package com.ritazcode.mailtracking.dto.postalItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO object for updating a postal item (i.e. updating its status)")
public class UpdateItemDto {
    @NotNull(message="postal item id is required")
    @Schema(description = "Postal item id")
    Long id;

    @NotNull(message="postal item id is required")
    @Schema(description = "Post office id")
    Long postOfficeId;
}
