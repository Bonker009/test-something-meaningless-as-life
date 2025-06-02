package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.entity.Skill;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    List<Skill> getAllSkill();
}
