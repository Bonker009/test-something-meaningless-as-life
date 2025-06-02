package com.kshrd.devconnect_springboot.service.implementation;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.ForbiddenException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.request.EvaluateDeveloperRequest;
import com.kshrd.devconnect_springboot.model.dto.request.HackathonRequest;
import com.kshrd.devconnect_springboot.model.dto.request.SubmitHackathonRequest;
import com.kshrd.devconnect_springboot.model.dto.response.HackathonResponse;
import com.kshrd.devconnect_springboot.model.entity.AppUser;
import com.kshrd.devconnect_springboot.model.entity.Certificate;
import com.kshrd.devconnect_springboot.model.entity.Hackathon;
import com.kshrd.devconnect_springboot.model.entity.JoinHackathon;
import com.kshrd.devconnect_springboot.model.mapper.HackathonMapper;
import com.kshrd.devconnect_springboot.respository.AuthRepository;
import com.kshrd.devconnect_springboot.respository.CertificateRepository;
import com.kshrd.devconnect_springboot.respository.HackathonRepository;
import com.kshrd.devconnect_springboot.respository.JoinHackathonRepository;
import com.kshrd.devconnect_springboot.service.EmailSenderService;
import com.kshrd.devconnect_springboot.service.HackathonService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HackathonServiceImplement implements HackathonService {
    private final HackathonRepository hackathonRepository;
    private final JoinHackathonRepository joinHackathonRepository;
    private final CertificateRepository certificateRepository;
    private final EmailSenderService emailSenderService;
    private final AuthRepository authRepository;
    private final HackathonMapper hackathonMapper;

    @Override
    public List<HackathonResponse> getAllHackathons(Integer page, Integer size, String title) {
        page = (page - 1) * size;
        List<Hackathon> hackathons = hackathonRepository.getAllHackathons(page, size, title);

        if (hackathons.isEmpty()) {
            throw new NotFoundException("No hackathons");
        }
        List<HackathonResponse> hackathonResponses = hackathonMapper.toDetailResponse(hackathons);
        for (HackathonResponse h : hackathonResponses) {
            h.setAmountParticipant(joinHackathonRepository.getParticipantAmount(h.getHackathonId()));
        }
        return hackathonResponses;
    }

    @Override
    public List<HackathonResponse> getJoinedHistory(Integer page, Integer size, String title) {
        page = (page - 1) * size;
        List<Hackathon> hackathons = hackathonRepository.getJoinedHistory(page, size, title, CurrentUser.appUserId);

        if (hackathons.isEmpty()) {
            throw new NotFoundException("No hackathons");
        }
        List<HackathonResponse> hackathonResponses = hackathonMapper.toDetailResponse(hackathons);
        for (HackathonResponse h : hackathonResponses) {
            h.setAmountParticipant(joinHackathonRepository.getParticipantAmount(h.getHackathonId()));
        }
        return hackathonResponses;
    }

    @Override
    public Integer getCountJoinedHistory(String title) {
        return hackathonRepository.getCountJoinedHistory(title, CurrentUser.appUserId);
    }

    @Override
    public Integer getCountAllHackathon(String title) {
        return hackathonRepository.getCountAllHackathon(title);
    }

    @Override
    public Integer getCountAllHackathonsByCurrentUser(String title) {
        return hackathonRepository.getCountAllHackathonsByCurrentUser(title, CurrentUser.appUserId);
    }

    @Override
    public Hackathon getHackathonById(UUID hackathonId) {
        Hackathon hackathon = hackathonRepository.getHackathonById(hackathonId);
        if (hackathon == null) {
            throw new NotFoundException("Hackathon not found with ID: " + hackathonId);
        } else {
            hackathon.setAmountParticipant(joinHackathonRepository.getParticipantAmount(hackathon.getHackathonId()));
        }
        return hackathon;
    }

    @Override
    public Hackathon updateHackathonById(UUID hackathonId, HackathonRequest request) {
        Hackathon hackathon = hackathonRepository.updateHackathonById(hackathonId, request);

        if (hackathon == null) {
            throw new NotFoundException("Hackathon not found with ID: " + hackathonId);
        } else if (!hackathon.getCreatorId().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not the owner");
        } else {
            hackathon.setAmountParticipant(joinHackathonRepository.getParticipantAmount(hackathonId));
            validateHackathonDates(request.getStartDate(), request.getEndDate());
            return hackathon;
        }
    }

    @Override
    public Hackathon createHackathon(HackathonRequest request) {
        if (!request.getEndDate().isAfter(request.getStartDate()))
            throw new BadRequestException("End date must be after start date");

        Hackathon hackathon = hackathonRepository.createHackathon(request, CurrentUser.appUserId);
        hackathon.setAmountParticipant(0);
        validateHackathonDates(request.getStartDate(), request.getEndDate());
        return hackathon;
    }

    @Override
    public void deleteHackathonById(UUID hackathonId) {
        Hackathon hackathon = hackathonRepository.getHackathonById(hackathonId);
        if (hackathon == null) {
            throw new NotFoundException("Hackathon not found with ID: " + hackathonId);
        }
        if (!hackathon.getCreatorId().getUserId().equals(CurrentUser.appUserId)) {
            throw new ForbiddenException("You are not the owner of this hackathon");
        }
        hackathonRepository.deleteHackathonById(hackathonId);
    }

    @Override
    public List<HackathonResponse> getAllHackathonsByCurrentUser(Integer page, Integer size, String title) {
        page = (page - 1) * size;
        List<Hackathon> hackathons = hackathonRepository.getAllHackathonsByCurrentUser(page, size, title, CurrentUser.appUserId);

        if (hackathons.isEmpty()) {
            throw new NotFoundException("No hackathons were found for your account. Please create a hackathon or check again later.");
        }
        List<HackathonResponse> hackathonResponses = hackathonMapper.toDetailResponse(hackathons);
        for (HackathonResponse h : hackathonResponses) {
            h.setAmountParticipant(joinHackathonRepository.getParticipantAmount(h.getHackathonId()));
        }
        return hackathonResponses;
    }

    @Override
    public void joinHackathon(UUID hackathonId) {
        if (joinHackathonRepository.getJoinHackathonById(hackathonId, CurrentUser.appUserId) != null) {
            throw new BadRequestException("You already join this hackathon");
        }
        else {
            joinHackathonRepository.joinHackathon(hackathonId, CurrentUser.appUserId);
        }
    }

    @Override
    public void submitHackathon(UUID hackathonId, SubmitHackathonRequest request) {
            joinHackathonRepository.submitHackathon(hackathonId, request, CurrentUser.appUserId);

    }

    @SneakyThrows
    @Override
    public void evaluateDeveloper(UUID hackathonId, List<EvaluateDeveloperRequest> evaluateDeveloperRequests) {
        Hackathon hackathon = hackathonRepository.getHackathonById(hackathonId);
        if (hackathon == null) {
            throw new NotFoundException("Hackathon not found");
        }
        if (!hackathon.getCreatorId().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not the owner of this hackathon");
        }

        HashSet<EvaluateDeveloperRequest> seen = new HashSet<>();
        for (EvaluateDeveloperRequest request : evaluateDeveloperRequests) {
            JoinHackathon join = joinHackathonRepository.getJoinHackathonByUserId(hackathonId, request.getUserId());
            if (join == null || join.getSubmission() == null || join.getSubmission().trim().isEmpty()) {
                throw new BadRequestException("Developer has not submitted the project yet.");
            }
            Certificate certificates = certificateRepository.getCertificateByUser(hackathonId, request.getUserId());
            if (certificates != null) {
                throw new BadRequestException("User already receive certificate in this hackathon");
            }
            // Step 1: Update the score
            Integer scores = request.getScore();
            joinHackathonRepository.evaluateDeveloper(hackathonId, request);

            // Step 2: Get the full score from the hackathon
            Integer fullScore = certificateRepository.getFullScoreByHackathonId(hackathonId);

            // Step 3: Check if fullScore is not null
            if (fullScore != null) {
                String description;
                LocalDateTime issuedDate = LocalDateTime.now();

                // Step 4: Certificate logic based on score
                if (scores >= (fullScore * 0.5) && scores <= fullScore) {
                    description = "Certificate of Achievement";
                    // Step 5: Insert certificate
                    Certificate certificate = certificateRepository.insertCertificate(
                            description,
                            issuedDate,
                            hackathonId,
                            request.getUserId()
                    );
                    // Step 6: Get the user's email using their userId
                    AppUser appUser = authRepository.getUserById(request.getUserId());
                    if (appUser != null && appUser.getEmail() != null && !appUser.getEmail().isEmpty()) {
                        emailSenderService.generatePdf(appUser.getEmail(), certificate);
                    }
                }
            }
        }
    }

    private void validateHackathonDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        if (startDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }

        if (endDate.isAfter(startDate.plusDays(90))) {
            throw new IllegalArgumentException("Hackathon duration must not exceed 90 days.");
        }
    }


}
