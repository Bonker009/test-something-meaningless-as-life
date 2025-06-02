package com.kshrd.devconnect_springboot.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileImageRequest {
    @NotNull
    @NotBlank
    @Schema(defaultValue = "profile")
    private String profileImageUrl;
}
