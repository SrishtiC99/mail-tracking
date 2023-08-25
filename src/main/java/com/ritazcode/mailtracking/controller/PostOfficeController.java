package com.ritazcode.mailtracking.controller;

import com.ritazcode.mailtracking.dto.postOffice.CreatePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.ResponsePostOfficeDto;
import com.ritazcode.mailtracking.dto.postOffice.UpdatePostOfficeDto;
import com.ritazcode.mailtracking.exception.ErrorResponse;
import com.ritazcode.mailtracking.service.PostOfficeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post-office")
@Tag(name = "Post office controller", description = "Manage post office operations")
public class PostOfficeController {
    private final PostOfficeService postOfficeService;

    @Operation(summary = "Get post office by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post office is returned",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePostOfficeDto.class))}),
            @ApiResponse(responseCode = "400", description = "post office id is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "post office is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/")
    public ResponseEntity<ResponsePostOfficeDto> view(@Parameter(description = "id of post office") @RequestParam("id") Long id) {
        return new ResponseEntity<>(postOfficeService.view(id), HttpStatus.OK);

    }

    @Operation(summary = "Add new post office")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "post office is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePostOfficeDto.class))}),
            @ApiResponse(responseCode = "400", description = "post office id is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<ResponsePostOfficeDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "created post office info", required = true,
                    content = @Content(schema =
                    @Schema(implementation = CreatePostOfficeDto.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody CreatePostOfficeDto newPostDto) {
        return new ResponseEntity<>(postOfficeService.create(newPostDto), HttpStatus.CREATED);
    }

    @Operation(summary = "update existing post office")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post office is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponsePostOfficeDto.class))}),
            @ApiResponse(responseCode = "400", description = "post office id is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "post office is not found",
                    content = @Content)
    })
    @PutMapping("/update")
    public ResponseEntity<ResponsePostOfficeDto> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "updated post office info", required = true,
                    content = @Content(schema =
                    @Schema(implementation = UpdatePostOfficeDto.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody UpdatePostOfficeDto updatedPost) {
        return new ResponseEntity<>(postOfficeService.update(updatedPost), HttpStatus.OK);

    }

    @Operation(summary = "Delete an existing post office")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post office is deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))}),
            @ApiResponse(responseCode = "400", description = "post office id is not valid",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "post office is not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> delete(
            @Parameter(description = "id of post office") @RequestParam("id") Long id) {
        postOfficeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
