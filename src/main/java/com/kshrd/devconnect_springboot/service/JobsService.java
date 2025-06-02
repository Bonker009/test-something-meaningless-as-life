package com.kshrd.devconnect_springboot.  service;


import java.util.List;
import java.util.UUID;
import com.kshrd.devconnect_springboot.model.dto.request.JobsRequest;
import com.kshrd.devconnect_springboot.model.dto.response.JobResponse;
import com.kshrd.devconnect_springboot.model.entity.Jobs;

public interface JobsService {
    Jobs getJobsById(UUID id);
    List<JobResponse> getAllJobs(Integer page, Integer size, String title, List<UUID> skills);
    JobResponse createJobs(JobsRequest entity);
    JobResponse updateJobs (UUID id, JobsRequest entity);
    void deleteJobs(UUID id);
    JobResponse updateStatusJobs(UUID id, Boolean status);
    List<JobResponse> getAllJobsByCreatorId(Integer page, Integer size, String title, List<UUID> skills);
    Integer getCountAllJob(Integer page, Integer size, String title, List<UUID> skills);
    Integer getCountJobsByCreatorId(Integer page, Integer size, String title, List<UUID> skills);
}
