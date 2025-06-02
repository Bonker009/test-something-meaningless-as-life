package com.kshrd.devconnect_springboot.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.jobBoard.JobBoard;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import java.io.IOException;
import java.sql.*;

@MappedTypes(JobBoard.class)
public class JobBoardTypeHandler extends BaseTypeHandler<JobBoard> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JobBoard parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setObject(i, objectMapper.writeValueAsString(parameter), Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error serializing JobBoard", e);
        }
    }

    @Override
    public JobBoard getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getString(columnName));
    }

    @Override
    public JobBoard getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public JobBoard getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getString(columnIndex));
    }

    private JobBoard convert(String json) throws SQLException {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, JobBoard.class);
        } catch (IOException e) {
            throw new SQLException("Error deserializing JobBoard", e);
        }
    }
}