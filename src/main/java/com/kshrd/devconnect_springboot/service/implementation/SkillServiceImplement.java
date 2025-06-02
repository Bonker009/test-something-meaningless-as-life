package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.model.entity.Skill;
import com.kshrd.devconnect_springboot.respository.SkillRepository;
import com.kshrd.devconnect_springboot.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillServiceImplement implements SkillService {
    private final SkillRepository skillRepository;

    @Override
    public List<Skill> getAllSkill() {
        return skillRepository.getAllSkill();
    }
}
