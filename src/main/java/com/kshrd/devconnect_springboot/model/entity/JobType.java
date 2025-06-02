package com.kshrd.devconnect_springboot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobType {
    UUID jobTypeId;
    String jobTypeName;
}
