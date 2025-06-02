package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.ResumeRequest;
import com.kshrd.devconnect_springboot.model.entity.Resume;
import com.kshrd.devconnect_springboot.service.ResumeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ResumeController extends BaseController {
    private final ResumeService resumesService;
    @GetMapping("get-resume")
    public ResponseEntity<ApiResponse<Resume>> getCurrentResume() {
        return response("Resumes retrieved successfully",resumesService.selectCurrentResumes());
        }
    @PostMapping("create-resume")
    public ResponseEntity<ApiResponse<Resume>> createResumes(@RequestBody @Valid ResumeRequest entity) {
        return response("Resumes created successfully", HttpStatus.CREATED, resumesService.createResumes(entity));
    }
    @PutMapping("update-resume")
    public ResponseEntity<ApiResponse<Resume>> updateResumes(@RequestBody @Valid ResumeRequest entity) {
        return response("Resumes updated successfully",resumesService.updateResumes(entity));
    }
    @DeleteMapping("delete-resume")
    public ResponseEntity<ApiResponse<Resume>> deleteResumes() {
        resumesService.deleteResumes();
        return response("Resumes deleted successfully", null);
    }
}
