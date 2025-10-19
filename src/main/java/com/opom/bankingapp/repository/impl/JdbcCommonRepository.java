package com.opom.bankingapp.repository.impl;

import com.opom.bankingapp.dto.common.OptionDto;
import com.opom.bankingapp.repository.CommonRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcCommonRepository implements CommonRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCommonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class OptionDtoRowMapper implements RowMapper<OptionDto> {
        @Override
        public OptionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OptionDto(
                rs.getInt("id"),
                rs.getString("name")
            );
        }
    }

    @Override
    public List<OptionDto> getGenderOptions() {
        String sql = "SELECT id, name FROM Gender";
        return jdbcTemplate.query(sql, new OptionDtoRowMapper());
    }

    @Override
    public List<OptionDto> getNationalityOptions() {
        String sql = "SELECT id, name FROM Nationality";
        return jdbcTemplate.query(sql, new OptionDtoRowMapper());
    }
}
