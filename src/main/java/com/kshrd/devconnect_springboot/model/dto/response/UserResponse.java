package com.kshrd.devconnect_springboot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
}
