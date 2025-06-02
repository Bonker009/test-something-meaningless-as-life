package com.kshrd.devconnect_springboot.model.JSONBTemplate.jobBoard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobBoard {
    private List<Requirement> requirements;
    private List<Responsibility> responsibilities;
    private List<Benefit> benefits;
}
