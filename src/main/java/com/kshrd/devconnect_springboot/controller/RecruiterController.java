package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.RecruiterRequest;
import com.kshrd.devconnect_springboot.model.entity.Recruiter;
import com.kshrd.devconnect_springboot.service.RecruiterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/recruiter-profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RecruiterController extends BaseController {
    private final RecruiterService recruiterService;
    @GetMapping
    public ResponseEntity<ApiResponse<Recruiter>> getRecruiterProfile() {
        return response("Get recruiter profile successfully", recruiterService.getRecruiterProfile());
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Recruiter>> updateRecruiterProfile(@Valid @RequestBody RecruiterRequest request) {
        return response("Update recruiter profile successfully", recruiterService.updateRecruiterProfile(request));
    }
}
