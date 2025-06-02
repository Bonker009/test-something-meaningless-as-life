package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.dto.request.RecruiterRequest;
import com.kshrd.devconnect_springboot.model.entity.Recruiter;

public interface RecruiterService {
    Recruiter getRecruiterProfile();
    Recruiter updateRecruiterProfile(RecruiterRequest request);
}
