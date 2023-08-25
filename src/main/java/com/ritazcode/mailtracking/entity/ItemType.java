package com.ritazcode.mailtracking.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = """
        postal item type:\s
        * `LETTER` - letter
        * `PARCEL` - parcel
        * `PACKAGE` - package
        * `POSTCARD` - postcard
        """)
public enum ItemType {
    LETTER,
    PARCEL,
    PACKAGE,
    POSTCARD
}
