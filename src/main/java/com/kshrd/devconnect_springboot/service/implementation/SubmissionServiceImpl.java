package com.kshrd.devconnect_springboot.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.model.dto.request.SubmissionRequest;
import com.kshrd.devconnect_springboot.model.dto.request.SubmitCodeRequest;
import com.kshrd.devconnect_springboot.model.dto.response.SubmissionResponse;
import com.kshrd.devconnect_springboot.model.entity.Submission;
import com.kshrd.devconnect_springboot.respository.CodeChallengeRepository;
import com.kshrd.devconnect_springboot.respository.SubmissionRepository;
import com.kshrd.devconnect_springboot.service.CodeChallengeService;
import com.kshrd.devconnect_springboot.service.SubmissionService;
import com.kshrd.devconnect_springboot.utils.CodeGenerator;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import com.kshrd.devconnect_springboot.utils.VersionLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final CodeChallengeService codeChallengeService;
    private final CodeChallengeRepository codeChallengeRepository;
    private final SubmissionRepository submissionRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PISTON_URL = "https://emkc.org/api/v2/piston/execute";

    // This method is used to get all submissions from the database
    @Override
    public List<SubmissionResponse> getAllSubmission() {
        List<Submission> submissions = submissionRepository.getAllSubmission();
        List<SubmissionResponse> submissionResponses = new ArrayList<>();

        for (Submission submission : submissions) {
            SubmissionResponse response = new SubmissionResponse();
            response.setName(submission.getDeveloperId().getFirstName() + " " + submission.getDeveloperId().getLastName());
            response.setSubmitTime(submission.getSubmitTime());
            response.setScore(submission.getScore());
            submissionResponses.add(response);
        }
        return submissionResponses;
    }

    // This method is used to create a submission in the database
    public void createSubmission(UUID challengeId, Long timeSubmitted, Double score) {
        if(!codeChallengeRepository.selectCodeChallengeById(challengeId).getChallengeId().equals(challengeId)){
            throw new BadRequestException("You have already submitted this challenge.");
        }
        if(codeChallengeRepository.selectCodeChallengeById(challengeId) == null){
            throw new BadRequestException("Challenge not found.");
        }
        SubmissionRequest entity = new SubmissionRequest();
        entity.setChallengeId(challengeId);
        entity.setDeveloperId(CurrentUser.appUserId);
        entity.setSubmitTime(timeSubmitted);
        entity.setSubmittedAt(LocalDateTime.now());
        entity.setScore(score);
        submissionRepository.insertSubmission(entity);
    }

    // This method is used to evaluate the user code
    public List<String> evaluateStudentCode(String studentCode, UUID codeId) throws JsonProcessingException {
        var challenge = codeChallengeService.getCodeChallengeById(codeId);
        var testCases = challenge.getTestCase();
        List<String> results = new ArrayList<>();
        CodeGenerator.ExtractedFunction extracted = CodeGenerator.extractParts(challenge.getStarterCode(), challenge.getLanguage());
        String fullCode = CodeGenerator.generate(
                challenge.getLanguage(),
                extracted.header(),
                studentCode,
                extracted.functionName(),
                testCases

        );

        System.out.println("Full Code: " + fullCode);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("language", challenge.getLanguage());
        requestBody.put("version", VersionLanguage.getVersion(challenge.getLanguage()));
        requestBody.put("files", List.of(Map.of("content", fullCode)));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(PISTON_URL, requestEntity, String.class);

        if (response == null) {
            results.add("No response from Piston API.");
            return results;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        String stderr = root.path("run").path("stderr").asText().trim();
        if (!stderr.isEmpty()) {
            results.add("Compilation/Runtime Error:\n" + stderr);
            return results;
        }

        String output = root.path("run").path("output").asText().trim();

        List<String> expectedOutputList = testCases.stream()
                .map(tc -> String.valueOf(tc.getExpectedOutput()).trim())
                .toList();

        String[] outputs = output.split("\\r?\\n");

        if (outputs.length != expectedOutputList.size()) {
            results.add("Mismatch in output count. Got: " + outputs.length + ", Expected: " + expectedOutputList.size());
            return results;
        }

        for (int i = 0; i < expectedOutputList.size(); i++) {
            if (!outputs[i].equals(expectedOutputList.get(i))) {
                results.add("Test " + (i + 1) +
                        " failed. output: " + outputs[i] +
                        ", Expected: " + expectedOutputList.get(i));
            } else {
                results.add("Test " + (i + 1) +
                        " passed. output: " + outputs[i] +
                        ", Expected: " + expectedOutputList.get(i));
            }
        }

        return results;
    }

    // This method is used to submit the user code
    @Override
    public List<String> submitCode(SubmitCodeRequest submitCodeRequest, UUID codeId) {
        try {
            List<String> results = evaluateStudentCode(submitCodeRequest.getCode(), codeId);
            double scores = codeChallengeRepository.selectCodeChallengeById(codeId).getScore();

            // Count passed tests
            long passedTests = results.stream()
                    .filter(result -> result.toLowerCase().contains("passed"))
                    .count();

            int totalTests = results.stream()
                    .filter(result -> result.toLowerCase().startsWith("test"))
                    .toArray().length;
            double maxScore = passedTests * scores / totalTests;
            // Calculate score based on time submitted 3600 seconds = 1 hour
            double time = 3600 - submitCodeRequest.getTimeSubmitted();
            double score = ((maxScore / 3600) * time);
            // Round score to 2 decimal places
            score = Math.round(score * 100.0) / 100.0;
            if(time < 0){
                score = 0;
            }
            // Save submission to database
            createSubmission(codeId, submitCodeRequest.getTimeSubmitted(), score);
            return results;
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Error processing JSON: " + e.getMessage());
        }
    }

    // This method is used to test the user code
    @Override
    public List<String> testStudentCode(SubmitCodeRequest studentCode, UUID codeId) {
        try {
            return evaluateStudentCode(studentCode.getCode(), codeId);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Error processing JSON: " + e.getMessage());
        }
    }
}