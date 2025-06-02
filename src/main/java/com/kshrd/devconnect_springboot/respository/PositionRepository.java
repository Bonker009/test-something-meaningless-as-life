package com.kshrd.devconnect_springboot.respository;

import com.kshrd.devconnect_springboot.config.UuidTypeHandler;
import com.kshrd.devconnect_springboot.model.entity.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.UUID;

@Mapper
public interface PositionRepository {
    @Result(property = "positionId", column = "position_id")
    @Result(property = "positionName", column = "position_name")
    @Select("""
        SELECT * FROM positions
    """)
    List<Position> getAllPosition();

    @Result(property = "positionId", column = "position_id")
    @Result(property = "positionName", column = "position_name")
    @Select("""
        SELECT * FROM positions WHERE position_id = #{positionId}
    """)
    Position getPositionById(UUID positionId);

    @Select("""
        SELECT position_name FROM positions WHERE position_id = #{positionId}
    """)
    String getPositionNameById(UUID positionId);
}
