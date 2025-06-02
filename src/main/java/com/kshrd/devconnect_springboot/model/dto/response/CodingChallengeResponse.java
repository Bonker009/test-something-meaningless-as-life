package com.kshrd.devconnect_springboot.model.dto.response;

import com.kshrd.devconnect_springboot.model.JSONBTemplate.TestCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingChallengeResponse {
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
    private Integer participation;
}
