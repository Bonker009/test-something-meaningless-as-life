package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.request.EvaluateDeveloperRequest;
import com.kshrd.devconnect_springboot.model.dto.request.SubmitHackathonRequest;
import com.kshrd.devconnect_springboot.model.entity.JoinHackathon;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface JoinHackathonRepository {

    @Results(id = "joinHackathonMapper", value = {
            @Result(property = "joinedAt", column = "joined_at"),
            @Result(property = "submittedAt", column = "submitted_at"),
            @Result(property = "developerId", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),
    })
    @Select("""
        SELECT * FROM join_hackathons
        WHERE hackathon_id = #{hackathonId}
        AND submitted_at IS NOT NULL
        ORDER BY score DESC, submitted_at ASC;
    """)
    List<JoinHackathon> getAllJoinHackathonByHackathonId(UUID hackathonId);

    @ResultMap("joinHackathonMapper")
    @Select("""
            INSERT INTO join_hackathons (hackathon_id, user_id)
            VALUES ( #{hackathonId}, #{userId});
            """)
    void joinHackathon(@Param("hackathonId") UUID hackathonId, @Param("userId") UUID userId);

    @ResultMap("joinHackathonMapper") // this is for checking if the developer already join the hackathon
    @Select("""
        SELECT * FROM join_hackathons WHERE hackathon_id = #{hackathonId} AND user_id = #{userId}
    """)
    JoinHackathon getJoinHackathonById(UUID hackathonId, UUID userId);

    @Update("""
                UPDATE join_hackathons
                SET submission = #{request.submissionLink}, submitted_at = CURRENT_TIMESTAMP
                WHERE hackathon_id = #{hackathonId} AND user_id = #{userId}
            """)
    void submitHackathon(@Param("hackathonId") UUID hackathonId,
                         @Param("request") SubmitHackathonRequest request,
                         @Param("userId") UUID userId);


    @ResultMap("joinHackathonMapper")
    @Update("""
            UPDATE join_hackathons
            SET score = #{request.score}
            WHERE hackathon_id = #{hackathonId} AND user_id = #{request.userId}
            """)
    void evaluateDeveloper(@Param("hackathonId") UUID hackathonId, @Param("request") EvaluateDeveloperRequest request);

    @Select("""
        SELECT COUNT(*) FROM join_hackathons WHERE hackathon_id = #{hackathonId}
    """)
    Integer getParticipantAmount(UUID hackathonId);

    @ResultMap("joinHackathonMapper")
    @Select("""
            SELECT jh.user_id, jh.hackathon_id, jh.submission
            FROM join_hackathons jh
            JOIN hackathons h ON h.hackathon_id = jh.hackathon_id
            WHERE jh.hackathon_id = #{hackathonId}
              AND jh.user_id = #{userId}
              AND jh.submission IS NOT NULL;
            """)
    JoinHackathon getJoinHackathonByUserId(UUID hackathonId, UUID userId);
}

