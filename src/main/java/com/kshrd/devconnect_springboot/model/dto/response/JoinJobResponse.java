package com.kshrd.devconnect_springboot.model.dto.response;

import com.kshrd.devconnect_springboot.model.entity.Jobs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinJobResponse {
    private String joinJobId;
    private Boolean isInvited;
    private JobResponse job;
    private UserResponse user;
}
