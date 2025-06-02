package com.kshrd.devconnect_springboot.  service.implementation;

import java.util.List;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.response.JobResponse;
import com.kshrd.devconnect_springboot.model.mapper.JobMapper;
import com.kshrd.devconnect_springboot.respository.*;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kshrd.devconnect_springboot.service.JobsService;
import com.kshrd.devconnect_springboot.model.entity.Jobs;
import com.kshrd.devconnect_springboot.model.dto.request.JobsRequest;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobsServiceImplementation implements JobsService {
    private final JobsRepository repository;
    private final JobSkillRepository jobSkillRepository;
    private final SkillRepository skillRepository;
    private final PositionRepository positionRepository;
    private final JobMapper jobMapper;
    private final JobsRepository jobsRepository;
    private final JoinJobRepository joinJobRepository;

    @Override
    public Jobs getJobsById(UUID jobId) {
        Jobs job = repository.selectJobsById(jobId);
        if (job == null) {
            throw new NotFoundException("Job not found");
        }
        return job;
    }

    @Override
    public List<JobResponse> getAllJobs(Integer page, Integer size, String title, List<UUID> skills) {
        page = (page - 1) * size;
        List<Jobs> jobs = repository.getAllJobs(page, size, title, skills);
        if(jobs == null || jobs.isEmpty()) {
            throw new NotFoundException("No jobs found");
        }
        return jobMapper.toResponse(jobs);
    }

    @Override
    public JobResponse createJobs(JobsRequest entity) {
        List<UUID> skillIds = entity.getSkillId();
        if (repository.selectJobTypeById(entity.getJobTypeId()) == null) {
            throw new NotFoundException("Job type not found");
        }
        if(positionRepository.getPositionById(entity.getPositionId()) == null) {
            throw new NotFoundException("Position not found");
        }
        for (UUID skillId : skillIds) {
            if (skillRepository.getSkillById(skillId) == null) {
                throw new NotFoundException("Skill not found");
            }
        }
        Jobs createJob = repository.insertJobs(entity, CurrentUser.appUserId);
        for (UUID skillId : skillIds) {
            jobSkillRepository.insertSkillByJobId(createJob.getJobId(), skillId);
        }
        return jobMapper.toResponse(repository.selectJobsById(createJob.getJobId()));
    }

    @Override
    public JobResponse updateJobs(UUID jobId, JobsRequest entity) {
        List<UUID> skillIds = entity.getSkillId();
        if (repository.selectJobsById(jobId) == null) {
            throw new NotFoundException("Job not found");
        }
        // check if the user is authorized to update the job
        if (!repository.selectJobsById(jobId).getCreator().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not authorized to update this job");
        }
        //check if position exists
        for (UUID skillId : skillIds) {
            if (skillRepository.getSkillById(skillId) == null) {
                throw new NotFoundException("Skill not found");
            }
        }
        // check if job type and position exists
        if (repository.selectJobTypeById(entity.getJobTypeId()) == null) {
            throw new NotFoundException("Job type not found");
        }
        if(positionRepository.getPositionById(entity.getPositionId()) == null) {
            throw new NotFoundException("Position not found");
        }
        if(joinJobRepository.getJoinJobByJobId(jobId) != null) {
            throw new NotFoundException("Cannot update job, there are developers who have joined this job");
        }
        repository.updateJobs(jobId, CurrentUser.appUserId, entity);
        // delete old skills
        jobSkillRepository.deleteSkillByJobId(jobId);
        // create new skills
        for (UUID skillId : skillIds) {
            jobSkillRepository.insertSkillByJobId(jobId, skillId);
        }
        return jobMapper.toResponse(repository.selectJobsById(jobId));
    }

    @Override
    public void deleteJobs(UUID jobId) {
    if (repository.selectJobsById(jobId) == null) {
            throw new NotFoundException("Job not found");
    }
    if(!repository.selectJobsById(jobId).getCreator().getUserId().equals(CurrentUser.appUserId)) {
        throw new BadRequestException("You are not authorized to delete this job");
    }
        repository.deleteJobs(jobId , CurrentUser.appUserId);
        jobSkillRepository.deleteSkillByJobId(jobId);
    }

    @Override
    public JobResponse updateStatusJobs(UUID jobId, Boolean status) {
        Jobs job = repository.selectJobsById(jobId);
        if (job == null) {
                throw new NotFoundException("Job not found");
        }
        return jobMapper.toResponse(repository.updateStatus(jobId, status , CurrentUser.appUserId));
    }

    @Override
    public List<JobResponse> getAllJobsByCreatorId(Integer page, Integer size, String title, List<UUID> skills) {
        page = (page - 1) * size;
        List<Jobs> jobs = repository.selectJobsByCreatorId(page, size, title, skills, CurrentUser.appUserId);
        if (jobs == null || jobs.isEmpty()) {
            throw new NotFoundException("No jobs found for the current user");
        }
        return jobMapper.toResponse(jobs);
    }

    @Override
    public Integer getCountAllJob(Integer page, Integer size, String title, List<UUID> skills) {
        return jobsRepository.getCountAllJob(page, size, title, skills);
    }

    @Override
    public Integer getCountJobsByCreatorId(Integer page, Integer size, String title, List<UUID> skills) {
        return jobsRepository.getCountJobsByCreatorId(page, size, title, skills, CurrentUser.appUserId);
    }



}
