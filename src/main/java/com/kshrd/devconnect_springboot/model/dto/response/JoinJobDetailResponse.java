package com.kshrd.devconnect_springboot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinJobDetailResponse {
    private String joinJobId;
    private String title;
    private String description;
    private String cv;
    private String coverLetter;
    private UserResponse user;
}
