package com.ritazcode.mailtracking.dto.postalItem;

import com.ritazcode.mailtracking.entity.ItemStatus;
import com.ritazcode.mailtracking.entity.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Postal item as a response object")
public class ResponseItemDto {

    @Schema(description = "Postal item id")
    Long id;

    @Schema(description = "Postal item type, it might be letter, parcel, package or postcard")
    ItemType type;

    @Schema(description = "Recipient index")
    String recipientIndex;

    @Schema(description = "Recipient address")
    String recipientAddress;

    @Schema(description = "Recipient name")
    String recipientName;

    @Schema(description = "Postal item status, it might be registered, arrived, departed or received")
    ItemStatus status;
}
