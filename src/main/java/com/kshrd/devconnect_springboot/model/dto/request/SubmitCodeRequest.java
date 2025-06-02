package com.kshrd.devconnect_springboot.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitCodeRequest {
    @NotBlank(message = "code is required")
    private String code;
    @NotNull(message = "time submit is required")
    private Long timeSubmitted;
}
