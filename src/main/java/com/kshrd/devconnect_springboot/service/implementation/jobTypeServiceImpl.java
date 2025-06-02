package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.entity.JobType;
import com.kshrd.devconnect_springboot.respository.JobsRepository;
import com.kshrd.devconnect_springboot.service.jobTypeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class jobTypeServiceImpl implements jobTypeService {
    private final JobsRepository jobRepository;

    @Override
    public List<JobType> getAllJobTypes() {
        List<JobType> jobTypes = jobRepository.getAllJobTypes();
        if (jobTypes == null || jobTypes.isEmpty()) {
            throw new NotFoundException("No job types found");
        }
        return jobTypes;
    }
}
