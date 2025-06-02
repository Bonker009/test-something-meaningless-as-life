package com.kshrd.devconnect_springboot.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.resumeInfomation.ResumeInformation;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.io.IOException;
import java.sql.*;
@MappedTypes(ResumeInformation.class)
public class ResumeInformationTypeHandler extends BaseTypeHandler<ResumeInformation> {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ResumeInformation parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setObject(i, objectMapper.writeValueAsString(parameter), Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error serializing ResumeInformation", e);
        }
    }

    @Override
    public ResumeInformation getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convert(rs.getString(columnName));
    }

    @Override
    public ResumeInformation getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public ResumeInformation getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convert(cs.getString(columnIndex));
    }

    private ResumeInformation convert(String json) throws SQLException {
        if (json == null || json.isEmpty()) return null;
        try {
            return objectMapper.readValue(json, ResumeInformation.class);
        } catch (IOException e) {
            throw new SQLException("Error deserializing ResumeInformation", e);
        }
    }
}