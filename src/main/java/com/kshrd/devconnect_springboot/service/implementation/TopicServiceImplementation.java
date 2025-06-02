package com.kshrd.devconnect_springboot.service.implementation;


import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.exception.NotFoundException;
import com.kshrd.devconnect_springboot.model.dto.request.TopicRequest;
import com.kshrd.devconnect_springboot.model.dto.response.TopicResponse;
import com.kshrd.devconnect_springboot.model.entity.Topic;
import com.kshrd.devconnect_springboot.model.mapper.TopicMapper;
import com.kshrd.devconnect_springboot.respository.SkillRepository;
import com.kshrd.devconnect_springboot.respository.TopicRepository;
import com.kshrd.devconnect_springboot.service.TopicService;
import com.kshrd.devconnect_springboot.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TopicServiceImplementation implements TopicService {
    private final TopicRepository repository;
    private final TopicMapper topicMapper;
    private final SkillRepository skillRepository;

    @Override
    public Topic getTopicsById(UUID id) {
        Topic topic = repository.selectTopicsById(id);
        if (topic == null) {
            throw new BadRequestException("Topic not found");
        }
        return topic;
    }

    @Override
    public List<TopicResponse> getAllTopics(Integer page, Integer size) {
        page = (page - 1) * size;
        List<Topic> topics = repository.getAllTopics(page, size);
        if (topics == null || topics.isEmpty()) {
            throw new NotFoundException("don't have topic to fetch");
        }
        return topicMapper.toShowInCard(topics);
    }

    @Override
    public Topic createTopics(TopicRequest entity) {
        List<UUID> skillIds = entity.getTags();
        HashSet<UUID> seen = new HashSet<>();
        // check if the skillIds are not existing in the database
        for (UUID skillId : skillIds) {
            if (!seen.add(skillId)) throw new BadRequestException("No duplicate skill allow");
            if (skillRepository.getSkillById(skillId) == null) {
                throw new NotFoundException("Skill not found");
            }
        }
        entity.setTitle(entity.getTitle().trim());
        entity.setContent(entity.getContent().trim());
        Topic topic = repository.insertTopics(entity, CurrentUser.appUserId);
        for (UUID skillId : skillIds) {
            repository.insertSkillToTopic(topic.getTopicId(), skillId);
        }
        return repository.selectTopicsById(topic.getTopicId());
    }

    @Override
    public Topic updateTopics(UUID id, TopicRequest entity) {
        Topic topic = repository.selectTopicsById(id);

        if (repository.selectTopicsById(id) == null) {
            throw new NotFoundException("Topic not found");
        }

        if (!topic.getCreator().getUserId().equals(CurrentUser.appUserId)) {
            throw new BadRequestException("You are not owner so you can't update this topic");
        }

        List<UUID> skillIds = entity.getTags();
        // check if the skillIds are not existing in the database
        for (UUID skillId : skillIds) {
            if (skillRepository.getSkillById(skillId) == null) {
                throw new NotFoundException("Skill not found");
            }
        }
        // delete existing skills for the topic
        repository.deleteSkillByTopicId(topic.getTopicId());
        // insert new skills for the topic
        for (UUID skillId : skillIds) {
            repository.insertSkillToTopic(topic.getTopicId(), skillId);
        }
        entity.setTitle(entity.getTitle().trim());
        entity.setContent(entity.getContent().trim());
        return repository.updateTopics(id, entity , CurrentUser.appUserId);
    }


    @Override
    public void deleteTopics(UUID id) {
        if (repository.selectTopicsById(id) == null) {
            throw new BadRequestException("Topic not found");
        }

        Topic topic = repository.selectTopicsById(id);
        if(!topic.getCreator().getUserId().equals(CurrentUser.appUserId)){
            throw new BadRequestException("You are not owner so you can't delete this topic");
        }
        repository.deleteTopics(id, CurrentUser.appUserId);
    }

    @Override
    public List<TopicResponse> getTopicsByCurrentUser(Integer page, Integer size) {
        page = (page - 1) * size;
        List<Topic> topics= repository.getTopicsByUserId(page, size, CurrentUser.appUserId);
        if (topics == null || topics.isEmpty()) {
            throw new NotFoundException("You don't have any topic to fetch");
        }
        return topicMapper.toShowInCard(topics);
    }
}
