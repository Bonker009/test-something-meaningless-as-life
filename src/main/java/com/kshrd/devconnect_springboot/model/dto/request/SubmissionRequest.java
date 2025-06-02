package com.kshrd.devconnect_springboot.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionRequest {
    private Double score;
    private Long submitTime;
    private UUID challengeId;
    private UUID developerId;
    private LocalDateTime submittedAt;
}
