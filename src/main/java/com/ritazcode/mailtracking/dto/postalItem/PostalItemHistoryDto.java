package com.ritazcode.mailtracking.dto.postalItem;

import com.ritazcode.mailtracking.entity.ItemStatus;
import com.ritazcode.mailtracking.entity.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostalItemHistoryDto {
    @Schema(description = "Postal item id")
    Long itemId;

    @Schema(description = "Postal item type, it might be letter, parcel, package or postcard")
    ItemType type;

    @Schema(description = "Recipient name")
    String recipientName;

    @Schema(description = "Postal item status, it might be registered, arrived, departed or received")
    ItemStatus status;

    @Schema(description = "Post office id")
    Long postOfficeId;

    @Schema(description = "Post office name")
    String postOfficeName;

    @Schema(description = "time of operation")
    Date timestamp;
}
