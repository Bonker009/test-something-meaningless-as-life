package com.kshrd.devconnect_springboot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isRecruiter;
    private Boolean isVerified;
    private String profileImageUrl;
    private LocalDateTime createdAt;
}
