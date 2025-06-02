package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.entity.Certificate;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper
public interface CertificateRepository {

    @Result(property = "issuedDate", column = "issued_date")
    @Result(property = "hackathon", column = "hackathon_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.HackathonRepository.getHackathonById"))
    @Result(property = "userResponse", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById"))
    @Select("""
                INSERT INTO hackathon_certificate (
                    description, issued_date, hackathon_id, user_id
                )
                VALUES (
                    #{description}, #{issuedDate}, #{hackathonId}, #{userId}
                )
                RETURNING *;
            """)
    Certificate insertCertificate(@Param("description") String description,
                                  @Param("issuedDate") LocalDateTime issuedDate,
                                  @Param("hackathonId") UUID hackathonId,
                                  @Param("userId") UUID userId);

    @Select("""
            SELECT full_score FROM hackathons WHERE hackathon_id = #{hackathonId}
            """)
    Integer getFullScoreByHackathonId(UUID hackathonId);

    @Select("""
        SELECT * FROM hackathon_certificate WHERE hackathon_id = #{hackathonId} AND user_id = #{userId}
    """)
    Certificate getCertificateByUser(UUID hackathonId, UUID userId);

}
