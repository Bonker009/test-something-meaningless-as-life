package com.kshrd.devconnect_springboot.model.mapper;

import com.kshrd.devconnect_springboot.model.dto.response.HackathonResponse;
import com.kshrd.devconnect_springboot.model.entity.Hackathon;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HackathonMapper {
    HackathonResponse toResponse(Hackathon hackathon);
    List<HackathonResponse> toDetailResponse(List<Hackathon> hackathons);
}
