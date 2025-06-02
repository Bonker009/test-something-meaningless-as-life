package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.model.entity.Position;
import com.kshrd.devconnect_springboot.respository.PositionRepository;
import com.kshrd.devconnect_springboot.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionServiceImplement implements PositionService {
    private final PositionRepository positionRepository;
    @Override
    public List<Position> getAllPositions() {
        return positionRepository.getAllPosition();
    }
}
