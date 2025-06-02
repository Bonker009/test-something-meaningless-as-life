package com.kshrd.devconnect_springboot.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateDeveloperRequest {
    private UUID userId;
    private Integer score;
}
