package com.ritazcode.mailtracking.dto.postalItem;

import com.ritazcode.mailtracking.entity.ItemType;
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
@Schema(description = "DTO object for registering a new postal item")
public class RegisterItemDto {

    @NotNull(message = "item type is required")
    @Schema(implementation = ItemType.class, enumAsRef = true)
    ItemType type;

    @NotNull(message = "Recipient index is required")
    @Schema(description = "recipient index")
    String recipientIndex;

    @NotNull(message = "Recipient address is required")
    @Schema(description = "recipient address")
    @Size(min = 10, max = 100, message = "recipient name length should be at least 10 and at most 100")
    String recipientAddress;

    @NotNull(message = "Recipient name is required")
    @Schema(description = "recipient name")
    @Size(min = 3, max = 30, message = "recipient name length should be at least 3 and at most 10")
    String recipientName;

}
