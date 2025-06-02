package com.kshrd.devconnect_springboot.  service;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.kshrd.devconnect_springboot.model.dto.request.InviteJobRequest;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobDetailResponse;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobResponse;
import com.kshrd.devconnect_springboot.model.entity.JoinJob;
import com.kshrd.devconnect_springboot.model.dto.request.JoinJobRequest;

public interface JoinJobService {
    JoinJobDetailResponse getJoinJobById(UUID id);
    List<JoinJobResponse> getAllJoinJob(Integer page, Integer size, UUID positionId, UUID jobType, String date);
    JoinJobResponse createJoinJob(JoinJobRequest entity , UUID id);
    void deleteJoinJob(UUID id);
    JoinJobResponse updateIsApprove(boolean isApprove , UUID joinJobId);
    List<JoinJobResponse> getAllJoinJobByIsApprove(Boolean isApprove);
    JoinJobResponse inviteJob(UUID jobId, UUID developerId, InviteJobRequest inviteJobRequest);
    List<JoinJobResponse> getAllInviteJob(UUID jobId);
    JoinJobResponse approveInviteJob(UUID joinJobId , UUID developerId);
    Integer getCountAllJoinJob(Integer page, Integer size, UUID positionId, UUID jobType, String date);
    List<JoinJobResponse> getJoinJobByJobId(UUID jobId , Boolean isApprove);

    List<JoinJobResponse> getAllInvite();
}
