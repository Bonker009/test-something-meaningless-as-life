package com.kshrd.devconnect_springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kshrd.devconnect_springboot.model.dto.request.SubmitCodeRequest;
import com.kshrd.devconnect_springboot.model.dto.response.SubmissionResponse;
import java.util.List;
import java.util.UUID;

public interface SubmissionService {
    List<SubmissionResponse> getAllSubmission();
    List<String> submitCode(SubmitCodeRequest studentCode, UUID codeId);
    List<String> testStudentCode(SubmitCodeRequest studentCode, UUID codeId) ;
}
