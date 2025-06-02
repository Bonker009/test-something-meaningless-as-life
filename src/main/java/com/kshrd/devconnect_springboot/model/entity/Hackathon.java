package com.kshrd.devconnect_springboot.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kshrd.devconnect_springboot.model.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hackathon {
    private UUID hackathonId;
    private String title;
    private String description;
    private Boolean isBookmark;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")

    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime createdDate = LocalDateTime.now();
    private Boolean isAvailable;
    private Integer amountParticipant;
    private Integer fullScore = 100;
    private UserResponse creatorId;
    private List<JoinHackathon> joinHackathons;
}
