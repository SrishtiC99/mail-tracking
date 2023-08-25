package com.ritazcode.mailtracking.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = """
        postal item status:\s
        * `REGISTERED` - registered by addressee
        * `ARRIVED` - arrived to post office
        * `DEPARTED` - departed from post office
        * `RECEIVED` - received by addressee\s
        """)
public enum ItemStatus {
    REGISTERED,
    ARRIVED,
    DEPARTED,
    RECEIVED
}
