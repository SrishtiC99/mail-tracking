package com.ritazcode.mailtracking.controller;

import com.ritazcode.mailtracking.dto.postalItem.PostalItemHistoryDto;
import com.ritazcode.mailtracking.dto.postalItem.RegisterItemDto;
import com.ritazcode.mailtracking.dto.postalItem.ResponseItemDto;
import com.ritazcode.mailtracking.dto.postalItem.UpdateItemDto;
import com.ritazcode.mailtracking.exception.ErrorResponse;
import com.ritazcode.mailtracking.service.PostalItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/postal-item")
@RequiredArgsConstructor
@Tag(name = "Postal Item Controller",
        description = "Manages postal item operations")

public class PostalItemController {
    private final PostalItemService postalItemService;

    @Operation(summary = "Get all postal items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "postal items list is returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Page.class)))}),
            @ApiResponse(responseCode = "400", description = "postal item id is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "postal item is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/all-items")
    public ResponseEntity<Page<ResponseItemDto>> getAllItems(
            @Parameter(description = "page number") @RequestParam(defaultValue = "1") final Integer pageNumber,
            @Parameter(description = "count of records in a page") @RequestParam(defaultValue = "2") final Integer pageSize) {
        return new ResponseEntity<>(postalItemService.getPostalItems(PageRequest.of(pageNumber - 1, pageSize)), HttpStatus.OK);

    }

    @Operation(summary = "Get postal item info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "postal item is returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Page.class)))}),
            @ApiResponse(responseCode = "400", description = "postal item id is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "postal item is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/view/{id}")
    public ResponseEntity<ResponseItemDto> view(@PathVariable Long id) {
        return new ResponseEntity<>(postalItemService.view(id), HttpStatus.OK);
    }

    @Operation(summary = "Get history of postal item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "postal items list is returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Page.class)))}),
            @ApiResponse(responseCode = "400", description = "postal item id is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "postal item is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/history")
    public ResponseEntity<Page<PostalItemHistoryDto>> showItemHistory(
            @Parameter(description = "page number") @RequestParam(defaultValue = "1") final Integer pageNumber,
            @Parameter(description = "count of records in a page") @RequestParam(defaultValue = "2") final Integer pageSize,
            @Parameter(description = "post item id") @RequestParam Long id) {
        return new ResponseEntity<>(postalItemService.getPostalItemHistory(id,PageRequest.of(pageNumber-1, pageSize)), HttpStatus.OK);
    }

    @Operation(summary = "Get post office postal items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "postal items list is returned",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Page.class)))}),
            @ApiResponse(responseCode = "400", description = "post office id is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "post office is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/all-office-items")
    public ResponseEntity<Page<ResponseItemDto>> getOfficeItems(
            @Parameter(description = "page number") @RequestParam(defaultValue = "1") final Integer pageNumber,
            @Parameter(description = "count of records in a page") @RequestParam(defaultValue = "2") final Integer pageSize,
            @Parameter(description = "id of post office") @RequestParam("id") Long id) {
        return new ResponseEntity<>(postalItemService.getOfficeItems(id,PageRequest.of(pageNumber - 1, pageSize)), HttpStatus.OK);

    }

    @Operation(summary = "Register a new postal item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "a new postal item is registered",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = ResponseItemDto.class)))}),
            @ApiResponse(responseCode = "400", description = "new postal item is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseItemDto> register(@RequestBody @Valid RegisterItemDto newItem) {
        return new ResponseEntity<>(postalItemService.register(newItem), HttpStatus.CREATED);
    }

    @Operation(summary = "Change postal item status to received to a specific office")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "postal status is updated",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = ResponseItemDto.class)))}),
            @ApiResponse(responseCode = "400",
                    description = """
                            postal item is not valid \t
                             post office id is not valid \t
                             item has already been RECEIVED""",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "postal item is not valid \t\n post office is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/arrive")
    public ResponseEntity<ResponseItemDto> arrive(@RequestBody @Valid UpdateItemDto updatedItem) {
        return new ResponseEntity<>(postalItemService.arriveToPostOffice(updatedItem), HttpStatus.OK);
    }

    @Operation(summary = "Change postal item status to departed from specific office")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "postal status is updated",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = ResponseItemDto.class)))}),
            @ApiResponse(responseCode = "400",
                    description = """
                            postal item is not valid \t
                             post office id is not valid \t
                             item has already been RECEIVED \t
                             item with can not DEPART from a post office to which it did NOT ARRIVE""",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "postal item is not valid \t\n post office is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/depart")
    public ResponseEntity<ResponseItemDto> send(@RequestBody @Valid UpdateItemDto updatedItem) {
        return new ResponseEntity<>(postalItemService.departFromPostOffice(updatedItem), HttpStatus.OK);
    }

    @Operation(summary = "Change postal item status to received by addressee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "postal status is updated",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
                    @Schema(implementation = ResponseItemDto.class)))}),
            @ApiResponse(responseCode = "400",
                    description = """
                            postal item is not valid \t
                             post office id is not valid \t
                             item has already been RECEIVED \t
                            \s""",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "postal item is not valid \t\n post office is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/receive")
    public ResponseEntity<ResponseItemDto> receive(@RequestBody @Valid UpdateItemDto updatedItem) {
        return new ResponseEntity<>(postalItemService.receiveByRecipient(updatedItem), HttpStatus.OK);
    }

}
