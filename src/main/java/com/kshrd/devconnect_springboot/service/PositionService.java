package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.entity.Position;

import java.util.List;
import java.util.UUID;

public interface PositionService {
    List<Position> getAllPositions();
}
