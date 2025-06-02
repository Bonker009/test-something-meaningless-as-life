package com.kshrd.devconnect_springboot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitHackathon {
    private LocalDateTime submittedAt;
}
