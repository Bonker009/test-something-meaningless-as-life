package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.request.TopicRequest;
import com.kshrd.devconnect_springboot.model.entity.Skill;
import com.kshrd.devconnect_springboot.model.entity.Topic;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface TopicRepository {
 
    // GET Topic BY ID
    @Select("""
        SELECT * FROM topics
        WHERE topic_id = #{topicId}
    """)
    @Results(id = "BaseResultMap", value = {
            @Result(property = "topicId", column = "topic_id"),
            @Result(property = "postedAt", column = "created_at"),
            @Result(property = "tags", column = "topic_id",
                    many = @Many(select = "com.kshrd.devconnect_springboot.respository.TopicRepository.getSkillByTopicId")),
            @Result(property = "creator", column = "user_id" ,
                    one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),
            @Result(property = "comments", column = "topic_id",
                    many = @Many(select = "com.kshrd.devconnect_springboot.respository.CommentRepository.selectCommentsByTopicId"))
    })
    Topic selectTopicsById(UUID topicId);
    
    // DELETE Topic
    @Delete("""
        DELETE
        FROM topics
        WHERE topic_id = #{topicId} AND user_id = #{creatorId}
        """)
    void deleteTopics(UUID topicId , UUID creatorId);

    // INSERT Topic
    @Select("""
        INSERT INTO topics
        (title, content, user_id)
        VALUES
        (
            #{topics.title},
            #{topics.content},

            #{creatorId}
        )
        RETURNING *;
        """)
        @ResultMap("BaseResultMap")
   
    Topic insertTopics(@Param("topics") TopicRequest entity , UUID creatorId);

    // UPDATE  Topic
    @Select("""
    UPDATE topics
    SET
        title = #{topics.title},
        content = #{topics.content}
    WHERE topic_id = #{id} AND user_id = #{creatorId}
    RETURNING *;
    """)
    @ResultMap("BaseResultMap")
    
    Topic updateTopics(UUID id , @Param("topics") TopicRequest entity , UUID creatorId);
    
    // GET ALL Topic
    @Select("""
        SELECT * FROM topics
          OFFSET #{page} LIMIT #{size}
    """)
    @ResultMap("BaseResultMap")
    List<Topic> getAllTopics(Integer page, Integer size);

    // ADD SKILL TO TOPIC
    @Insert("""
        INSERT INTO topic_skills (topic_id, skill_id)
        VALUES (#{topicId}, #{skillId})
    """)
    void insertSkillToTopic(@Param("topicId") UUID topicId, @Param("skillId") UUID skillId);

    // GET SKILL BY TOPIC ID
    @Select("""
        SELECT s.skill_name
        FROM skills s
        JOIN topic_skills ts ON s.skill_id = ts.skill_id
        WHERE ts.topic_id = #{topicId}
    """)
    List<String> getSkillByTopicId(UUID topicId);

    @Delete("""
    DELETE FROM topic_skills
    WHERE topic_id = #{topicId}
    """)
    void deleteSkillByTopicId(UUID topicId);

    @Select("""
    SELECT *
    FROM topics
    WHERE user_id = #{userId}
    OFFSET #{page} LIMIT #{size}
    """)
    @ResultMap("BaseResultMap")
    List<Topic> getTopicsByUserId(Integer page, Integer size, UUID userId);

}
