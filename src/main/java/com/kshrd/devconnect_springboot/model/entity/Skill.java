package com.kshrd.devconnect_springboot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    private UUID skillId;
    private String skillName;
}
