package com.kshrd.devconnect_springboot.service.implementation;

import java.util.List;

import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.TestCase;
import com.kshrd.devconnect_springboot.model.dto.request.CodeChallengeRequest;
import com.kshrd.devconnect_springboot.model.dto.response.CodingChallengeResponse;
import com.kshrd.devconnect_springboot.model.dto.response.CodingChallengeResponseCard;
import com.kshrd.devconnect_springboot.model.entity.CodeChallenge;
import com.kshrd.devconnect_springboot.model.mapper.CodingChallengeMapper;
import com.kshrd.devconnect_springboot.respository.CodeChallengeRepository;
import com.kshrd.devconnect_springboot.service.CodeChallengeService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CodeChallengeServiceImplement implements CodeChallengeService {
    private final CodeChallengeRepository repository;
    private final CodingChallengeMapper codingChallengeMapper;

    @Override
    public CodingChallengeResponse getCodeChallengeById(UUID id) {
        CodeChallenge codeChallenge = repository.selectCodeChallengeById(id);
        if(codeChallenge == null) {
            throw new NotFoundException("Code Challenge not found");
        }
        return codingChallengeMapper.toResponse(codeChallenge);
    }

    @Override
    public List<CodingChallengeResponseCard> getAllCodeChallenge() {
        List<CodeChallenge> codeChallenges = repository.getAllCodeChallenge();
        if(codeChallenges == null || codeChallenges.isEmpty()) {
            throw new NotFoundException("No Code Challenges found");
        }
        return codingChallengeMapper.toResponseCard(codeChallenges);
    }

    @Override
    public CodingChallengeResponse createCodeChallenge(CodeChallengeRequest codeChallengeRequest) {

        codeChallengeRequest.setStarterCode(codeChallengeRequest.getStarterCode().trim());
        codeChallengeRequest.setDescription(codeChallengeRequest.getDescription().trim());
        codeChallengeRequest.setTitle(codeChallengeRequest.getTitle().trim());
        codeChallengeRequest.setInstruction(codeChallengeRequest.getInstruction().trim());
        codeChallengeRequest.setLanguage(codeChallengeRequest.getLanguage().trim());

        CodeChallenge codeChallenge = repository.insertCodeChallenge(codeChallengeRequest , CurrentUser.appUserId);

        return codingChallengeMapper.toResponse(codeChallenge);

    }

    @Override
    public CodingChallengeResponse updateCodeChallenge(UUID id, CodeChallengeRequest codeChallengeRequest) {
        if(repository.selectCodeChallengeById(id) == null) {
            throw new NotFoundException("Code Challenge not found for update");
        }
        CodeChallenge codeChallenge = repository.selectCodeChallengeById(id);
        if (!codeChallenge.getCreator().getUserId().equals(CurrentUser.appUserId)) {
            throw new NotFoundException("You are not authorized to update this code challenge");
        }

        codeChallengeRequest.setStarterCode(codeChallengeRequest.getStarterCode().trim());
        codeChallengeRequest.setDescription(codeChallengeRequest.getDescription().trim());
        codeChallengeRequest.setTitle(codeChallengeRequest.getTitle().trim());
        codeChallengeRequest.setInstruction(codeChallengeRequest.getInstruction().trim());
        codeChallengeRequest.setLanguage(codeChallengeRequest.getLanguage().trim());


        return codingChallengeMapper.toResponse(repository.updateCodeChallenge(id, codeChallengeRequest));
    }

    @Override
    public List<CodingChallengeResponseCard> getAllCodeChallengesByUserId() {
        List<CodeChallenge> codeChallenge = repository.getAllCodeChallengesByUserId(CurrentUser.appUserId);
        if (codeChallenge == null) {
            throw new NotFoundException("you have not created any code challenges yet");
        }
        return codingChallengeMapper.toResponseCard(codeChallenge);
    }

    @Override
    public void deleteCodeChallenge(UUID id) {
        CodeChallenge codeChallenge = repository.selectCodeChallengeById(id);
        if(codeChallenge == null) {
            throw new NotFoundException("Code Challenge not found for deletion");
        }
        if (!codeChallenge.getCreator().getUserId().equals(CurrentUser.appUserId)) {
            throw new NotFoundException("You are not authorized to delete this code challenge");
        }
        repository.deleteCodeChallenge(id);
    }
}
