package com.kshrd.devconnect_springboot.  service.implementation;

import java.time.LocalDate;
import java.util.List;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.request.InviteJobRequest;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobDetailResponse;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobResponse;
import com.kshrd.devconnect_springboot.model.entity.Jobs;
import com.kshrd.devconnect_springboot.model.mapper.JoinJobDetailMapper;
import com.kshrd.devconnect_springboot.model.mapper.JoinJobMapper;
import com.kshrd.devconnect_springboot.respository.AppUserRepository;
import com.kshrd.devconnect_springboot.respository.DeveloperProfilesRepository;
import com.kshrd.devconnect_springboot.respository.JobsRepository;
import com.kshrd.devconnect_springboot.respository.JoinJobRepository;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.kshrd.devconnect_springboot.service.JoinJobService;
import com.kshrd.devconnect_springboot.model.entity.JoinJob;
import com.kshrd.devconnect_springboot.model.dto.request.JoinJobRequest;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JoinJobServiceImplementation implements JoinJobService {

    private final JoinJobRepository repository;
    private final JoinJobMapper joinJobMapper;
    private final JoinJobDetailMapper joinJobDetailMapper;
    private final JobsRepository jobsRepository;
    private final AppUserRepository appUserRepository;
    private final JoinJobRepository joinJobRepository;

    @Override
    public JoinJobDetailResponse getJoinJobById(UUID joinJobId) {
        JoinJob joinJob = repository.selectJoinJobById(joinJobId);
        if (joinJob == null) {
            throw new NotFoundException("JoinJob not found");
        }
        return joinJobDetailMapper.toResponse(joinJob);
    }

    @Override
    public List<JoinJobResponse> getAllJoinJob(Integer page, Integer size, UUID positionId, UUID jobType, String date) {
        page = (page - 1) * size;
        List<JoinJob> joinJob = repository.getAllJoinJob(page, size, CurrentUser.appUserId, positionId, jobType, date);
        if (joinJob == null || joinJob.isEmpty()) {
            throw new NotFoundException("No Join Jobs found");
        }
        return joinJobMapper.toResponse(joinJob);
    }

   // This method is used to create a new apply job for a specific job.
    @Override
    public JoinJobResponse createJoinJob(JoinJobRequest entity , UUID jobId) {
        if (jobsRepository.selectJobsById(jobId) == null) {
            throw new NotFoundException("Job not found");
        }
        if (repository.getJobByUserId(CurrentUser.appUserId) != null) {
            throw new BadRequestException("You are already applied for this job");
        }
        return joinJobMapper.toResponse(repository.insertJoinJob(entity , CurrentUser.appUserId , jobId));
    }

    // This method is used to (developer) reject invite and (recruiter) cancel invite .
    @Override
    public void deleteJoinJob(UUID joinJobId) {
        JoinJob joinJob = repository.selectJoinJobById(joinJobId);
         if (joinJob == null) {
            throw new NotFoundException("Join Job not found");
        }
        Jobs job = jobsRepository.selectJobsById(joinJob.getJob().getJobId());
        UUID creatorId = job.getCreator().getUserId();
        UUID developerId = joinJob.getUser().getUserId();

        // Check if the user is authorized to delete the job
        if(!creatorId.equals(CurrentUser.appUserId) && !developerId.equals(CurrentUser.appUserId)) {
            throw new BadRequestException("Cannot delete this job, you are not authorized");
        }
        repository.deleteJoinJob(joinJobId);
    }

    // This method is for recruiter used to approve apply job.
    @Override
    public JoinJobResponse updateIsApprove(boolean isApprove, UUID joinJobId) {
        JoinJob joinJob = repository.selectJoinJobById(joinJobId);
        if (joinJob == null) {
            throw new NotFoundException("JoinJob not found");
        }
        if(joinJob.getCoverLetter() == null) {
            throw new BadRequestException("This is job you invited developer to apply, not applied by developer");
        }
        if (joinJob.getJob().getCreator().getUserId() != CurrentUser.appUserId) {
            throw new BadRequestException("You are not authorized to approve this job");
        }
        return joinJobMapper.toResponse(repository.updateIsApprove(isApprove, joinJobId));
    }

    // This method is used to get all apply by status(approve or not approve).
    @Override
    public List<JoinJobResponse> getAllJoinJobByIsApprove(Boolean isApprove) {
        List<JoinJob> joinJobs = repository.getAllJoinJobByStatus(isApprove);
        if (joinJobs == null || joinJobs.isEmpty()) {
            throw new NotFoundException("No Join Jobs found");
        }
        return joinJobMapper.toResponse(joinJobs);
    }

    // This method is used to invite developer to apply for a job.
    @Override
    public JoinJobResponse inviteJob(UUID JobId, UUID developerId, InviteJobRequest inviteJobRequest) {
        if (jobsRepository.selectJobsById(JobId) == null) {
            throw new NotFoundException("Job not found");
        }
        if(repository.getJobByDevId(CurrentUser.appUserId) != null) {
            throw new BadRequestException("You are already invited to this job");
        }
        if (appUserRepository.getUserById(developerId) == null) {
            throw new NotFoundException("Developer not found");
        }
        if (jobsRepository.selectJobsById(JobId) == null) {
            throw new BadRequestException("Job not found");
        }
        if(!jobsRepository.selectJobsById(JobId).getCreator().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not authorized to invite developer for this job");
        }
        return joinJobMapper.toResponse(repository.inviteJoinJob(inviteJobRequest,developerId ,JobId, true));
    }

    // This method is used to get all invite job by job id.
    @Override
    public List<JoinJobResponse> getAllInviteJob(UUID jobId) {
        List<JoinJob> joinJobs = repository.getAllInvitation(jobId);
        if(joinJobs == null || joinJobs.isEmpty()) {
            throw new NotFoundException("No Join Jobs found");
        }
        return joinJobMapper.toResponse(joinJobs);
    }

    // This method is used to approve invite job by developer.
    @Override
    public JoinJobResponse approveInviteJob(UUID joinJobId , UUID developerId) {
        if (repository.selectJoinJobById(joinJobId) == null) {
            throw new NotFoundException("Join Job not found");
        }
        if(developerId.equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not authorized to approve this job");
        }
        return joinJobMapper.toResponse(repository.approveInviteJob(joinJobId , developerId));
    }

    @Override
    public Integer getCountAllJoinJob(Integer page, Integer size, UUID positionId, UUID jobType, String date) {
        return joinJobRepository.getCountAllJoinJob(page, size, CurrentUser.appUserId, positionId, jobType, date);
    }

    @Override
    public List<JoinJobResponse> getJoinJobByJobId(UUID jobId , Boolean isApprove) {
        List<JoinJob> joinJob = joinJobRepository.getAllApplicationByJobId(jobId ,isApprove , CurrentUser.appUserId);
        if(joinJob == null || joinJob.isEmpty()) {
            throw new NotFoundException("No Join Jobs found for this job");
        }
        return joinJobMapper.toResponse(joinJob);
    }

    @Override
    public List<JoinJobResponse> getAllInvite() {
        List<JoinJob> joinJobs = repository.getAllInviteByCurrentUser(CurrentUser.appUserId);
        if(joinJobs == null || joinJobs.isEmpty()) {
            throw new NotFoundException("No invitations found");
        }
        return joinJobMapper.toResponse(joinJobs);
    }


}
