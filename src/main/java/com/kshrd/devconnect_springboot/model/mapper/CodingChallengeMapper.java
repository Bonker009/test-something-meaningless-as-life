package com.kshrd.devconnect_springboot.model.mapper;

import com.kshrd.devconnect_springboot.model.dto.response.CodingChallengeResponse;
import com.kshrd.devconnect_springboot.model.dto.response.CodingChallengeResponseCard;
import com.kshrd.devconnect_springboot.model.dto.response.JoinJobDetailResponse;
import com.kshrd.devconnect_springboot.model.entity.CodeChallenge;
import com.kshrd.devconnect_springboot.model.entity.JoinJob;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CodingChallengeMapper {
    CodingChallengeResponse toResponse(CodeChallenge codeChallenge);
    List<CodingChallengeResponse> toResponse(List<CodeChallenge> codeChallenges);
    CodingChallengeResponseCard toResponseCard(CodeChallenge codeChallenge);
    List<CodingChallengeResponseCard> toResponseCard(List<CodeChallenge> codeChallenges);
}
