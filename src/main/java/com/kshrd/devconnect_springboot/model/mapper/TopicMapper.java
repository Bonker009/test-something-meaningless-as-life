package com.kshrd.devconnect_springboot.model.mapper;

import com.kshrd.devconnect_springboot.model.dto.response.TopicResponse;
import com.kshrd.devconnect_springboot.model.entity.Topic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    List<TopicResponse> toShowInCard(List<Topic> topics);
}
