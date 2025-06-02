package com.kshrd.devconnect_springboot.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteJobRequest {
    @NotNull(message = "Title is required")
    private String title;
    @NotNull(message = "Description is required")
    private String description;
}
