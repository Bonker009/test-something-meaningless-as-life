package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.dto.request.ResumeRequest;
import com.kshrd.devconnect_springboot.model.entity.Resume;

import java.util.List;
import java.util.UUID;

public interface ResumeService {
    Resume selectCurrentResumes();
    Resume createResumes(ResumeRequest entity);
    Resume updateResumes (ResumeRequest entity);
    void deleteResumes();
}
