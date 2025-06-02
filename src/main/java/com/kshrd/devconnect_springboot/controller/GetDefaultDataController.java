package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.entity.DeveloperBadge;
import com.kshrd.devconnect_springboot.model.entity.JobType;
import com.kshrd.devconnect_springboot.model.entity.Position;
import com.kshrd.devconnect_springboot.model.entity.Skill;
import com.kshrd.devconnect_springboot.respository.JobsRepository;
import com.kshrd.devconnect_springboot.service.BadgeService;
import com.kshrd.devconnect_springboot.service.PositionService;
import com.kshrd.devconnect_springboot.service.SkillService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class GetDefaultDataController extends BaseController {
    private final SkillService skillService;
    private final BadgeService badgeService;
    private final PositionService positionService;
    private final JobsRepository jobsRepository;


    @GetMapping("get-all-skills")
    public ResponseEntity<ApiResponse<List<Skill>>> getAllSkill() {
        return response("Get all skills successfully!!!", skillService.getAllSkill());
    }

    @GetMapping("get-badge-current-user")
    public ResponseEntity<ApiResponse<DeveloperBadge>> getBadgeCurrentUser() {
        return response("Badge retrieved successfully", badgeService.getBadgeCurrentUser());
    }

    @GetMapping("get-all-positions")
    public ResponseEntity<ApiResponse<List<Position>>> getAllPositions() {
        return response("Get all positions successfully!!!", positionService.getAllPositions());
    }

    @GetMapping("get-all-job-types")
    public ResponseEntity<ApiResponse<List<JobType>>> getAllJobTypes() {
        return response("Get all job types successfully!!!",jobsRepository.getAllJobTypes());
    }
}
