package com.kshrd.devconnect_springboot.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitHackathonRequest {
    private String submissionLink;
}