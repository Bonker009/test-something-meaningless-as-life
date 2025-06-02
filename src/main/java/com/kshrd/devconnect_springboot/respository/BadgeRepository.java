package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.model.entity.Badges;
import com.kshrd.devconnect_springboot.model.entity.DeveloperBadge;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface BadgeRepository {
    @Select("""
        SELECT *
        FROM developer_badges
        WHERE user_id = #{developerId}
        """)

    @Result(property = "badges", column = "badge_id",
            one = @One(select = "com.kshrd.devconnect_springboot.respository.BadgeRepository.getBadgesById"))
    DeveloperBadge getBadgesByCurrentUser(UUID badgeId);

    @Select("""
        SELECT *
        FROM badges
        WHERE badge_id = #{badgeId}
        """)
    @Results(id = "BadgeResultMap", value = {
            @Result(property = "badgeId", column = "badge_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "description", column = "description"),
            @Result(property = "icon", column = "icon")
    })
    Badges getBadgesById(UUID badgeId);
}
