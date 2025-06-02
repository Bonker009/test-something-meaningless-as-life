package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.dto.request.AppUserRequest;
import com.kshrd.devconnect_springboot.model.dto.request.AuthRequest;
import com.kshrd.devconnect_springboot.model.dto.request.PasswordRequest;
import com.kshrd.devconnect_springboot.model.dto.request.ProfileImageRequest;
import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.AuthResponse;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest) throws Exception;

    AppUserResponse register(AppUserRequest appUserRequest) throws MessagingException;

    void verify(String email, String optCode);

    void resend(String email) throws MessagingException;

    void forgotPassword(String email);

    void verifyForgot(String email, String otpCode);

    AppUserResponse resetPassword(String email, String otpCode, String newPassword);

    void verifyOldPassword(PasswordRequest passwordRequest);

    void changePassword(PasswordRequest passwordRequest);
}
