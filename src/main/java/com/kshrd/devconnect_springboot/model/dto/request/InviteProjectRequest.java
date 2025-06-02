package com.kshrd.devconnect_springboot.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteProjectRequest {
    @NotNull
    @NotBlank
    @Schema(defaultValue = "title")
    private String title;

    @NotNull
    @NotBlank
    @Schema(defaultValue = "description")
    private String description;

    @NotNull
    @NotBlank
    private List<UUID> developerId;

    @NotNull
    @NotBlank
    private UUID positionId;
}
