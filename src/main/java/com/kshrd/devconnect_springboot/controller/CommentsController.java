package com.kshrd.devconnect_springboot.controller;


import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.CommentRequest;
import com.kshrd.devconnect_springboot.model.entity.Comment;
import com.kshrd.devconnect_springboot.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentsController extends BaseController {

    private final CommentService commentsService;

    @PutMapping("/{commentId}")
    @Operation(summary = "Update a Comment by ID")
    public ResponseEntity<ApiResponse<Comment>> updateComments(@PathVariable UUID commentId, @RequestBody CommentRequest entity) {
         return response("Comment have been updated successfully", commentsService.updateComments(commentId,entity));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete a Comment by ID")
    public ResponseEntity<ApiResponse<Object>> deleteComments(@PathVariable UUID commentId) {
        commentsService.deleteComments(commentId);
        return response("Comment have been deleted successfully");
    }
}
