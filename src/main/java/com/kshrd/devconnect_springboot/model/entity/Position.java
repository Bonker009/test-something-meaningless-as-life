package com.kshrd.devconnect_springboot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    private UUID positionId;
    private String positionName;
}
