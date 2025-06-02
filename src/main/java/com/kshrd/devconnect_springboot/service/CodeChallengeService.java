package com.kshrd.devconnect_springboot.service;


import com.kshrd.devconnect_springboot.model.dto.request.CodeChallengeRequest;
import com.kshrd.devconnect_springboot.model.dto.response.CodingChallengeResponse;
import com.kshrd.devconnect_springboot.model.dto.response.CodingChallengeResponseCard;
import com.kshrd.devconnect_springboot.model.entity.CodeChallenge;

import java.util.List;
import java.util.UUID;

public interface CodeChallengeService {
    CodingChallengeResponse getCodeChallengeById(UUID id);
    List<CodingChallengeResponseCard> getAllCodeChallenge();
    CodingChallengeResponse createCodeChallenge(CodeChallengeRequest entity);
    CodingChallengeResponse updateCodeChallenge (UUID id, CodeChallengeRequest entity);
    void deleteCodeChallenge(UUID id);
    List<CodingChallengeResponseCard> getAllCodeChallengesByUserId();
}
