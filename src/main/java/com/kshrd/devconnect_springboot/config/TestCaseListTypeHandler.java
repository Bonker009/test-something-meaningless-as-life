package com.kshrd.devconnect_springboot.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.TestCase;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class TestCaseListTypeHandler extends BaseTypeHandler<List<TestCase>> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<TestCase> parameter, JdbcType jdbcType) throws SQLException {
        try {
            // Use setObject with Types.OTHER for jsonb
            ps.setObject(i, objectMapper.writeValueAsString(parameter), Types.OTHER);
        } catch (JsonProcessingException e) {
            throw new SQLException("Error serializing List<TestCase> to JSON", e);
        }
    }

    @Override
    public List<TestCase> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return convertJsonToTestCaseList(json);
    }

    @Override
    public List<TestCase> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return convertJsonToTestCaseList(json);
    }

    @Override
    public List<TestCase> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return convertJsonToTestCaseList(json);
    }

    private List<TestCase> convertJsonToTestCaseList(String json) throws SQLException {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, TestCase.class));
        } catch (IOException e) {
            throw new SQLException("Error deserializing JSON to List<TestCase>", e);
        }
    }
}