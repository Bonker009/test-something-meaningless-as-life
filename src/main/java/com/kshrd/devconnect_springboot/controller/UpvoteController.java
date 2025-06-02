package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.entity.Upvote;
import com.kshrd.devconnect_springboot.service.UpvoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/upvote")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UpvoteController extends BaseController {

    private final UpvoteService upvotesService;

    @PostMapping("/{commentId}")
    @Operation(summary = "Upvote Comment")
    public ResponseEntity<ApiResponse<Upvote>> createUpvote(@PathVariable UUID commentId) {
        upvotesService.createUpvote(commentId);
        return response("Upvote have been created successfully",HttpStatus.CREATED,null);
    }
    @DeleteMapping("/un-upvote/{commentId}")
    @Operation(summary = "Un-Upvote Comment")
    public ResponseEntity<ApiResponse<Upvote>> deleteUpvote(@PathVariable UUID commentId) {
        upvotesService.deleteUpvote(commentId);
        return response("Upvote have been deleted successfully", null );
    }
}
