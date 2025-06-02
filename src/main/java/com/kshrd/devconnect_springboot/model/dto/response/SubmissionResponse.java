package com.kshrd.devconnect_springboot.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {
    private String name;
    private Double score;
    private Long submitTime;
}
