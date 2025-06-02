package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.dto.request.EvaluateDeveloperRequest;
import com.kshrd.devconnect_springboot.model.dto.request.HackathonRequest;
import com.kshrd.devconnect_springboot.model.dto.request.SubmitHackathonRequest;
import com.kshrd.devconnect_springboot.model.dto.response.HackathonResponse;
import com.kshrd.devconnect_springboot.model.entity.Hackathon;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import com.kshrd.devconnect_springboot.utils.SqlQueryProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.UUID;

public interface HackathonService {

    List<HackathonResponse> getAllHackathons(Integer page, Integer size, String title);

    Hackathon getHackathonById(UUID hackathonId);

    Hackathon updateHackathonById(UUID hackathonId, HackathonRequest request);

    Hackathon createHackathon(HackathonRequest request);

    void deleteHackathonById(UUID hackathonId);

    List<HackathonResponse> getAllHackathonsByCurrentUser(Integer page, Integer size, String title);

    void joinHackathon(UUID hackathonId);

    void submitHackathon(UUID hackathonId, SubmitHackathonRequest request);

    void evaluateDeveloper(UUID hackathonId, List<EvaluateDeveloperRequest> request);

    List<HackathonResponse> getJoinedHistory(Integer page,Integer size,String title);

    Integer getCountJoinedHistory(String title);

    Integer getCountAllHackathon(String title);

    Integer getCountAllHackathonsByCurrentUser(String title);
}