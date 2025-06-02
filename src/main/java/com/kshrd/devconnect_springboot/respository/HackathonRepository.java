package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.request.EvaluateDeveloperRequest;
import com.kshrd.devconnect_springboot.model.dto.request.HackathonRequest;
import com.kshrd.devconnect_springboot.model.dto.request.SubmitHackathonRequest;
import com.kshrd.devconnect_springboot.model.entity.Hackathon;
import com.kshrd.devconnect_springboot.model.entity.JoinHackathon;
import com.kshrd.devconnect_springboot.utils.SqlQueryProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface HackathonRepository {

    @Results(id = "hackathonMapping", value = {
            @Result(property = "hackathonId", column = "hackathon_id"),
            @Result(property = "startDate", column = "started_at"),
            @Result(property = "endDate", column = "finished_at"),
            @Result(property = "createdDate", column = "created_at"),
            @Result(property = "isAvailable", column = "is_available"),
            @Result(property = "isBookmark", column = "is_bookmark"),
            @Result(property = "creatorId", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById")),    //Recruiter Id
            @Result(property = "fullScore", column = "full_score"),
            @Result(property = "developerId", column = "developer_id"),
            @Result(property = "joinHackathons", column = "hackathon_id", many = @Many(select = "com.kshrd.devconnect_springboot.respository.JoinHackathonRepository.getAllJoinHackathonByHackathonId"))
    })
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllHackathon")
    List<Hackathon> getAllHackathons(Integer page, Integer size, String title);

    @ResultMap("hackathonMapping")
    @SelectProvider(type = SqlQueryProvider.class, method = "getJoinedHistory")
    List<Hackathon> getJoinedHistory(Integer page, Integer size, String title, UUID appUserId);

    @ResultMap("hackathonMapping")
    @Select("""
            SELECT * FROM hackathons
            WHERE hackathon_id = #{hackathonId}
            """)
    Hackathon getHackathonById(UUID hackathonId);

    @ResultMap("hackathonMapping")
    @Select("""
            UPDATE hackathons
            SET title = #{req.title},
                description = #{req.description},
                started_at = #{req.startDate},
                finished_at = #{req.endDate},
                is_available = #{req.isAvailable}
            WHERE hackathon_id = #{hackathonId}
            RETURNING *;
            """)
    Hackathon updateHackathonById(UUID hackathonId, @Param("req") HackathonRequest request);

    @ResultMap("hackathonMapping")
    @Select("""
            INSERT INTO hackathons (title, description, started_at, finished_at,  is_available, user_id) VALUES (
                #{req.title},
                #{req.description},
                #{req.startDate},
                #{req.endDate},
                #{req.isAvailable},
                #{creatorId}
            )
           RETURNING *;
            """)
    Hackathon createHackathon(@Param("req") HackathonRequest request, @Param("creatorId") UUID creatorId);

    @Delete("""
            DELETE FROM hackathons WHERE hackathon_id = #{hackathonId}
            """)
    void deleteHackathonById(UUID hackathonId);

    @ResultMap("hackathonMapping")
    @SelectProvider(type = SqlQueryProvider.class, method = "getAllHackathonsByCurrentUser")
    List<Hackathon> getAllHackathonsByCurrentUser(Integer page, Integer size, String title, UUID creatorId);

    @SelectProvider(type = SqlQueryProvider.class, method = "getCountAllHackathon")
    Integer getCountAllHackathon(String title);

    @SelectProvider(type = SqlQueryProvider.class, method = "getCountJoinedHistory")
    Integer getCountJoinedHistory(String title, UUID userId);

    @SelectProvider(type = SqlQueryProvider.class, method = "getCountAllHackathonsByCurrentUser")
    Integer getCountAllHackathonsByCurrentUser(String title, UUID creatorId);

}