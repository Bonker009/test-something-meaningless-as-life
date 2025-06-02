package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.entity.Bookmark;
import com.kshrd.devconnect_springboot.model.enums.BookmarkEnum;
import com.kshrd.devconnect_springboot.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/bookmark")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookmarkController extends BaseController {
    private final BookmarkService bookmarkService;

    @GetMapping
    @Operation(summary = "Get all user's bookmark")
    public ResponseEntity<ApiResponse<List<?>>> getAllBookmarkByUser(
            @RequestParam @Schema(description = "choose target type") BookmarkEnum bookmarkType,
            @RequestParam(defaultValue = "1") @Positive Integer page,
            @RequestParam(defaultValue = "10") @Positive Integer size
            ) {
        return response("Get all bookmark", bookmarkService.bookmarkByType(bookmarkType, page, size), page, size, bookmarkService.bookmarkPagination(bookmarkType));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete bookmark")
    public ResponseEntity<ApiResponse<Object>> deleteBookmark(@RequestParam UUID targetId) {
        bookmarkService.deleteBookmark(targetId);
        return response("Delete bookmark successfully");
    }

    @PostMapping("/create")
    @Operation(summary = "Create bookmark")
    public ResponseEntity<ApiResponse<Bookmark>> createProfileBookmark(
            @RequestParam UUID targetId) {
        return response("Create bookmark successfully", HttpStatus.CREATED ,bookmarkService.createBookmark(targetId));
    }

}
