package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.dto.request.PasswordRequest;
import com.kshrd.devconnect_springboot.model.dto.request.ProfileImageRequest;
import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController extends BaseController {

    private final AuthService authService;

    @PostMapping("/verify-password")
    @Operation(summary = "Verify old password")
    public ResponseEntity<ApiResponse<Object>> verifyOldPassword(@RequestBody PasswordRequest passwordRequest) {
        authService.verifyOldPassword(passwordRequest);
        return response("successful");
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<ApiResponse<Object>> changePassword(@RequestBody PasswordRequest passwordRequest) {
        authService.changePassword(passwordRequest);
        return response("successful");
    }
}
