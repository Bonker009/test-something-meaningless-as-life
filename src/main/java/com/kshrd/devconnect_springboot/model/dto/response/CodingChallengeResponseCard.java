package com.kshrd.devconnect_springboot.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingChallengeResponseCard {
    private String challengeId;
    private String title;
    private String description;
    private String createdAt;
    private UserResponse creator;
    private String language;
    private String profileUser;
    private Integer participation;
}
