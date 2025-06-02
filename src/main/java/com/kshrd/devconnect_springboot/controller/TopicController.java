package com.kshrd.devconnect_springboot.controller;


import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.TopicRequest;
import com.kshrd.devconnect_springboot.model.dto.response.TopicResponse;
import com.kshrd.devconnect_springboot.model.entity.Topic;
import com.kshrd.devconnect_springboot.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/v1/topics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TopicController extends BaseController {

    private final TopicService topicsService;

    @GetMapping
    @Operation(summary = "Get all topics")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getAllTopics(@RequestParam(defaultValue = "1") @Positive Integer page,
                                                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<TopicResponse> topics = topicsService.getAllTopics(page, size);
        return response("Topics retrieved successfully", topics);
    }

    @GetMapping("/{TopicId}")
    @Operation(summary = "Get topic by id")
    public ResponseEntity<ApiResponse<Topic>> getTopicsById(@PathVariable UUID TopicId) {
        return response("Topics retrieved by id successfully", topicsService.getTopicsById(TopicId));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new topic")
    public ResponseEntity<ApiResponse<Topic>> createTopics(@RequestBody @Valid TopicRequest entity) {
        return response("Topics have been created successfully", HttpStatus.CREATED, topicsService.createTopics(entity));
    }

    @PutMapping("/{TopicId}")
    @Operation(summary = "Update a topic by ID")
    public ResponseEntity<ApiResponse<Topic>> updateTopics(@PathVariable UUID TopicId, @RequestBody @Valid TopicRequest entity) {
        return response("Topics have been updated successfully", topicsService.updateTopics(TopicId,entity));
    }

    @DeleteMapping("/{TopicId}")
    @Operation(summary = "Delete a topic by ID")
    public ResponseEntity<ApiResponse<Object>> deleteTopics(@PathVariable UUID TopicId) {
        topicsService.deleteTopics(TopicId);
        return response("Topics have been deleted successfully");
    }

    @GetMapping("/current-user")
    @Operation(summary = "Get topics by current user")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getTopicsByCurrentUser(@RequestParam(defaultValue = "1") @Positive Integer page,
                                                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<TopicResponse> topics = topicsService.getTopicsByCurrentUser(page, size);
        return response("Topics retrieved by current user successfully", topics);
    }
}
