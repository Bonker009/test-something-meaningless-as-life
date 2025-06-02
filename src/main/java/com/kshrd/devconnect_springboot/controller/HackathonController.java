package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.EvaluateDeveloperRequest;
import com.kshrd.devconnect_springboot.model.dto.request.HackathonRequest;
import com.kshrd.devconnect_springboot.model.dto.response.HackathonResponse;
import com.kshrd.devconnect_springboot.model.entity.Hackathon;
import com.kshrd.devconnect_springboot.model.dto.request.SubmitHackathonRequest;
import com.kshrd.devconnect_springboot.service.HackathonService;
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
@RequiredArgsConstructor
@RequestMapping("api/v1/hackathons")
@SecurityRequirement(name = "bearerAuth")
public class HackathonController extends BaseController {
    private final HackathonService hackathonService;

    @GetMapping
    @Operation(summary = "Get all hackathons")
    public ResponseEntity<ApiResponse<List<HackathonResponse>>> getAllHackathons(
            @RequestParam(defaultValue = "1")  @Positive Integer page,
            @RequestParam(defaultValue = "10")  @Positive Integer size,
            @RequestParam(required = false) String title) {
        return response("All Hackathons fetched successfully", hackathonService.getAllHackathons(page, size, title), page, size, hackathonService.getCountAllHackathon(title));
    }

    @GetMapping("/history")
    @Operation(summary = "History of joined hackathon")
    public ResponseEntity<ApiResponse<List<HackathonResponse>>> getJoinedHistory( @RequestParam(defaultValue = "1") @Positive Integer page,
                                                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                                                  @RequestParam(required = false) String title) {
        return response("All Joined hackathons fetched successfully", hackathonService.getJoinedHistory(page, size, title), page, size, hackathonService.getCountJoinedHistory(title));
    }

    @GetMapping("/{hackathon-id}")
    @Operation(summary = "Get a hackathon by ID")
    public ResponseEntity<ApiResponse<Hackathon>> getHackathonById(@PathVariable("hackathon-id") UUID hackathonId) {
        return response("Hackathon ID < " + hackathonId + " > Founded", hackathonService.getHackathonById(hackathonId));
    }

    @GetMapping("/recruiter")
    @Operation(summary = "Get hackathons by Current user")
    public ResponseEntity<ApiResponse<List<HackathonResponse>>> getAllHackathonsByCurrentUser( @RequestParam(defaultValue = "1") @Positive Integer page,
                                                                                               @RequestParam(defaultValue = "10") @Positive Integer size,
                                                                                               @RequestParam(required = false) String title) {
        return response("Hackathons have been successfully fetched", hackathonService.getAllHackathonsByCurrentUser(page, size, title), page, size, hackathonService.getCountAllHackathonsByCurrentUser(title));
    }

    @PutMapping("/{hackathon-id}")
    @Operation(summary = "Update a hackathon by ID")
    public ResponseEntity<ApiResponse<Hackathon>> updateHackathonById(@PathVariable("hackathon-id") UUID hackathonId, @Valid @RequestBody HackathonRequest request) {
        return response("Hackathon ID " + hackathonId + " Founded", hackathonService.updateHackathonById(hackathonId, request));
    }

    @PostMapping
    @Operation(summary = "Create a hackathon")
    public ResponseEntity<ApiResponse<Hackathon>> createHackathon(@Valid @RequestBody HackathonRequest request) {
        return response("A Hackathon created successfully", HttpStatus.CREATED,hackathonService.createHackathon(request));
    }

    @DeleteMapping("/{hackathon-id}")
    @Operation(summary = "Delete a hackathon by ID")
    public ResponseEntity<ApiResponse<Object>> deleteHackathonById(@PathVariable("hackathon-id") UUID hackathonId) {
        hackathonService.deleteHackathonById(hackathonId);
        return response("You Deleted a hackathon with ID << " + hackathonId + " >> successfully");
    }

    @PostMapping("/join-hackathon/{hackathon-id}")
    @Operation(summary = "Join hackathon")
    public ResponseEntity<ApiResponse<Object>> joinHackathon(@RequestParam UUID hackathonId) {
        hackathonService.joinHackathon(hackathonId);
        return response("You joined a hackathon");
    }

    @PatchMapping("/submit-hackathon/{hackathon-id}")
    @Operation(summary = "Submit hackathon")
    public ResponseEntity<ApiResponse<Object>> submitHackathon(@PathVariable("hackathon-id") UUID hackathonId, @Valid @RequestBody SubmitHackathonRequest request) {
        hackathonService.submitHackathon(hackathonId, request);
        return response("You submitted successfully");
    }

    @PatchMapping("/evaluate_developer/{hackathon-id}")
    @Operation(summary = "Evaluate joined developer")
    public ResponseEntity<ApiResponse<Object>> evaluateDeveloper(@PathVariable("hackathon-id") UUID hackathonId, @Valid @RequestBody List<EvaluateDeveloperRequest> request) {
        hackathonService.evaluateDeveloper(hackathonId, request);
        return response("Developer evaluation completed successfully");
    }

//    @GetMapping()
//    @Operation(summary = "Mark mvp in one hackathon")
//    public ResponseEntity<ApiResponse<Object>> updateMvpHackathon() {

//    }
}
