package com.kshrd.devconnect_springboot.model.entity;

import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certificate {
    private String description;
    private LocalDateTime issuedDate;
    private Hackathon hackathon;
    private UserResponse userResponse;
}
