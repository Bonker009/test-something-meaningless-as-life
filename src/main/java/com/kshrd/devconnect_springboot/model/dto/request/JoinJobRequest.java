package com.kshrd.devconnect_springboot.  model.dto.request;
    
    

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinJobRequest {
    @NotNull(message = "Job ID is required")
    private String title;
    @NotNull(message = "Description is required")
    private String description;
    @NotNull(message = "Cover letter is required")
    private String coverLetter;
}
