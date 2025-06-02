package com.kshrd.devconnect_springboot.model.entity;


import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
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
public class Submission {
       private String submissionId;
       private Double score;
       private Long submitTime;
       private UUID challengeId;
       private AppUserResponse developerId;
       private LocalDateTime submittedAt;
}
