package com.kshrd.devconnect_springboot.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPositionRequest {
    @Positive
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Must be greater than 0")
    private Integer maxMembers;

    @NotNull
    @NotBlank
    private UUID positionId;
}
