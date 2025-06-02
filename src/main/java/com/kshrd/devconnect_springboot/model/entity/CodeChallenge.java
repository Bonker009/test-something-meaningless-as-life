package com.kshrd.devconnect_springboot.model.entity;
    
    

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.kshrd.devconnect_springboot.model.JSONBTemplate.TestCase;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeChallenge {
       private UUID challengeId;
       private String title;
       private String description;
       private String instruction;
       private List<TestCase> testCase;
       private LocalDate createdAt;
       private Integer score;
       private UserResponse creator;
       private String starterCode;
       private String language;
       private String profileUser;
       private Integer participation;
}
