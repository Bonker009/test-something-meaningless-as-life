package com.kshrd.devconnect_springboot.model.entity;

import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinHackathon {
    private Integer score;
    private LocalDateTime joinedAt;
    private String submission;
    private LocalDateTime submittedAt;
    private UserResponse developerId;
}
