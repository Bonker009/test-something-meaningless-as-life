package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.dto.request.RecruiterRequest;
import com.kshrd.devconnect_springboot.model.dto.response.AppUserResponse;
import com.kshrd.devconnect_springboot.model.entity.Recruiter;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.UUID;

@Mapper
public interface RecruiterRepository {
    @Results(id = "baseMapper", value = {
            @Result(property = "recruiterId", column = "recruiter_id"),
            @Result(property = "companyName", column = "company_name"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "companyLocation", column = "company_location"),
            @Result(property = "foundedAt", column = "founded_at"),
            @Result(property = "coverPicture", column = "cover_picture"),
            @Result(property = "userInformation", column = "user_id", one = @One(select = "com.kshrd.devconnect_springboot.respository.AppUserRepository.getUserResponseById"))
    })
    @Select("""
        SELECT * FROM recruiter_profile r WHERE user_id = #{userId}
    """)
    Recruiter getRecruiterProfile(UUID userId);

    @ResultMap("baseMapper")
    @Insert("""
        INSERT INTO recruiter_profile (user_id)
        VALUES (
             #{userId}
    )
        RETURNING *;
    """)
    void createRecruiterProfile(UUID userId);

    @ResultMap("baseMapper")
    @Select("""
        UPDATE recruiter_profile SET company_name = #{req.companyName},
                                           phone_number = #{req.phoneNumber},
                                           industry = #{req.industry},
                                           company_location = #{req.companyLocation},
                                           bio = #{req.bio},
                                           company_site = #{req.companySite},
                                           founded_at = #{req.foundedAt},
                                           cover_picture = #{req.coverPicture}
    WHERE user_id = #{userId}
    RETURNING *;
    """)
    Recruiter updateRecruiterProfile(UUID userId, @Param("req") RecruiterRequest request);
}
