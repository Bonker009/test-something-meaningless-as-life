package com.kshrd.devconnect_springboot.  model.entity;
    
    

import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.dto.response.JobResponse;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinJob {
       private UUID joinJobId;
       private String title;
       private String description;
       private Boolean isApprove;
       private Boolean isInvited;
       private String cv;
       private String coverLetter;
       private JobResponse job;
       private UserResponse user;
}
