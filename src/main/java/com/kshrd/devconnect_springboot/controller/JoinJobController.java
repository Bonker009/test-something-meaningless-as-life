package com.kshrd.devconnect_springboot.  controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.InviteJobRequest;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobDetailResponse;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import com.kshrd.devconnect_springboot.service.JoinJobService;
import com.kshrd.devconnect_springboot.model.entity.JoinJob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.kshrd.devconnect_springboot.model.dto.request.JoinJobRequest;
import org.springframework.http.HttpStatus;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/join-jobs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class JoinJobController  extends BaseController {

    private final JoinJobService joinJobService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<JoinJobResponse>>> getAllJoinJob(@RequestParam(defaultValue = "1") @Positive Integer page,
                                                                            @RequestParam(defaultValue = "10") @Positive Integer size,
                                                                            @RequestParam(required = false) UUID positionId,
                                                                            @RequestParam(required = false) UUID jobType,
                                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String date) {
        return response("Join job retrieved successfully", joinJobService.getAllJoinJob(page, size, positionId, jobType, date),
                page, size, joinJobService.getCountAllJoinJob(page, size, positionId, jobType, date));
    }

    @GetMapping("/{join-job-id}")
    public ResponseEntity<ApiResponse<JoinJobDetailResponse>> getJoinJobById(@PathVariable("join-job-id") UUID joinId) {
        return response("Join job retrieved by id successfully", joinJobService.getJoinJobById(joinId));
    }

    @PostMapping("/{job-id}")
    public ResponseEntity<ApiResponse<JoinJobResponse>> createJoinJob(@RequestBody JoinJobRequest entity , @PathVariable("job-id") UUID jobId) {
        return response("Join job have been created successfully", HttpStatus.CREATED, joinJobService.createJoinJob(entity , jobId));
    }

    @PutMapping("/{join-job-id}")
    public ResponseEntity<ApiResponse<JoinJobResponse>> updateIsApprove(@PathVariable("join-job-id") UUID joinId, @RequestParam boolean isApprove) {
        return response("Approve developer successfully", joinJobService.updateIsApprove(isApprove , joinId));
    }

    @GetMapping("/approve-application")
    @Operation(summary = "list all join job by status")
    public ResponseEntity<ApiResponse<List<JoinJobResponse>>> getAllJoinJobByIsApprove(@RequestParam Boolean isApprove) {
        return response("Join job retrieved successfully", joinJobService.getAllJoinJobByIsApprove(isApprove));
    }

    @PostMapping("/invite-job/{job-id}/{developer-id}")
    @Operation(summary = "invite developer to job")
    public ResponseEntity<ApiResponse<JoinJobResponse>> inviteJob(@PathVariable("job-id") UUID jobId, @PathVariable("developer-id") UUID devId, @RequestBody InviteJobRequest inviteJobRequest) {
        return response("Invite job successfully", joinJobService.inviteJob(jobId, devId, inviteJobRequest));
    }

    @GetMapping("/invite-job/{job-id}")
    @Operation(summary = "list all developer who are invited to job")
    public ResponseEntity<ApiResponse<List<JoinJobResponse>>> getAllInviteJob(@PathVariable("job-id") UUID jobId) {
        return response("Invite job retrieved successfully", joinJobService.getAllInviteJob(jobId));
    }

    @DeleteMapping("/delete-invite/{join-job-id}")
    @Operation(summary = "cancel invite job and reject invite")
    public ResponseEntity<ApiResponse<JoinJobResponse>> cancelInviteJob(@PathVariable("join-job-id") UUID joinId) {
        joinJobService.deleteJoinJob(joinId);
        return response("Delete invite job successfully",null);
    }

    @PostMapping("/approve-invite/{join-job-id}")
    @Operation(summary = "approve invite job")
    public ResponseEntity<ApiResponse<JoinJobResponse>> approveInviteJob(@PathVariable("join-job-id") UUID joinId) {
        return response("Approve invite job successfully", joinJobService.updateIsApprove(true, joinId));
    }

    @GetMapping("/job-owner/{job-id}")
    @Operation(summary = "get all apply by job id by current user")
    public ResponseEntity<ApiResponse<List<JoinJobResponse>>> getJoinJobByJobId(@PathVariable("job-id") UUID jobId, @RequestParam Boolean isApprove) {
        return response("Join job retrieved by job id successfully", joinJobService.getJoinJobByJobId(jobId, isApprove));
    }

    @GetMapping("/all-invitation")
    @Operation(summary = "get all invitation by current user")
    public ResponseEntity<ApiResponse<List<JoinJobResponse>>> getAllInvitation() {
        return response("Invitation retrieved successfully", joinJobService.getAllInvite());
    }

}
