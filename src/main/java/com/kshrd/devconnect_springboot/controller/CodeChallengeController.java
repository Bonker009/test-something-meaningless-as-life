package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.CodeChallengeRequest;
import com.kshrd.devconnect_springboot.model.dto.response.CodingChallengeResponse;
import com.kshrd.devconnect_springboot.model.dto.response.CodingChallengeResponseCard;
import com.kshrd.devconnect_springboot.model.entity.CodeChallenge;
import com.kshrd.devconnect_springboot.service.CodeChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/code-challenge")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CodeChallengeController extends BaseController {

    private final CodeChallengeService codeChallengeService;

    @GetMapping("/get-all")
    @Operation(summary = "Get all code challenges")
    public ResponseEntity<ApiResponse<List<CodingChallengeResponseCard>>>  getAllCodeChallenge() {
        return response("CodeChallenge retrieved successfully", codeChallengeService.getAllCodeChallenge());
    }

    @GetMapping("/get-by-id/{codeId}")
    @Operation(summary = "Get a code challenge by ID")
    public ResponseEntity<ApiResponse<CodingChallengeResponse>> getCodeChallengeById(@PathVariable UUID codeId) {
        return response("CodeChallenge retrieved successfully", codeChallengeService.getCodeChallengeById(codeId));
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new code challenge")
    public ResponseEntity<ApiResponse<CodingChallengeResponse>> createCodeChallenge(@RequestBody @Valid CodeChallengeRequest entity) {
        return response("CodeChallenge created successfully",
                        HttpStatus.CREATED,
                        codeChallengeService.createCodeChallenge(entity));

    }

    @PutMapping("/update/{codeId}")
    @Operation(summary = "Update a code challenge by ID")
    public ResponseEntity<ApiResponse<CodingChallengeResponse>> updateCodeChallenge(@PathVariable UUID codeId, @RequestBody @Valid CodeChallengeRequest entity) {
        return response("CodeChallenge updated successfully", codeChallengeService.updateCodeChallenge(codeId,entity));
    }

    @DeleteMapping("/delete/{codeId}")
    @Operation(summary = "Delete a code challenge by ID")
    public ResponseEntity<ApiResponse<CodeChallenge>> deleteCodeChallenge(@PathVariable UUID codeId) {
        codeChallengeService.deleteCodeChallenge(codeId);
        return response("CodeChallenge updated successfully", null);
    }

    @GetMapping("/by-user")
    @Operation(summary = "Get all code challenges by Current User")
    public ResponseEntity<ApiResponse<List<CodingChallengeResponseCard>>> getAllCodeChallengesByUserId() {
        return response("CodeChallenges retrieved successfully", codeChallengeService.getAllCodeChallengesByUserId());
    }

}
