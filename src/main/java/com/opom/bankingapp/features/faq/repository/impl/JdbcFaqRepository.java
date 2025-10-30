package com.opom.bankingapp.features.faq.repository.impl;

import com.opom.bankingapp.dto.faq.FaqCategoryResponse;
import com.opom.bankingapp.dto.faq.FaqDetailResponse;
import com.opom.bankingapp.dto.faq.FaqRequest;
import com.opom.bankingapp.dto.faq.FaqStatus;
import com.opom.bankingapp.features.faq.repository.FaqRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcFaqRepository implements FaqRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFaqRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class FaqDetailResponseRowMapper implements RowMapper<FaqDetailResponse> {
        @Override
        public FaqDetailResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            FaqCategoryResponse category = new FaqCategoryResponse(
                    rs.getInt("faq_category_id"),
                    rs.getString("category_name")
            );

            return new FaqDetailResponse(
                    rs.getLong("id"),
                    rs.getString("question"),
                    rs.getString("answer"),
                    FaqStatus.PUBLISHED.getValue(),
                    category,
                    rs.getTimestamp("created_at").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    Optional.ofNullable(rs.getTimestamp("updated_at"))
                            .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                            .orElse(null)
            );
        }
    }

    private static final FaqDetailResponseRowMapper FAQ_MAPPER = new FaqDetailResponseRowMapper();

    @Override
    public Long save(Long createdBy, FaqRequest request) {
        String sql = """
            INSERT INTO Faq (question, answer, faq_category_id)
            VALUES (?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.question());
            ps.setString(2, request.answer());
            ps.setInt(3, request.faqCategoryId());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Long faqId, Long updatedBy, FaqRequest request) {
        String sql = """
            UPDATE Faq
            SET question = ?,
                answer = ?,
                faq_category_id = ?,
                updated_at = NOW()
            WHERE id = ?
        """;
        jdbcTemplate.update(sql,
            request.question(),
            request.answer(),
            request.faqCategoryId(),
            faqId
        );
    }

    @Override
    public void delete(Long faqId) {
        String sql = "DELETE FROM Faq WHERE id = ?";
        jdbcTemplate.update(sql, faqId);
    }

    private static final String FIND_ALL_SQL = """
        SELECT f.id, f.question, f.answer, f.faq_category_id, f.created_at, f.updated_at, fc.name AS category_name
        FROM Faq f
        LEFT JOIN Faq_category fc ON f.faq_category_id = fc.id
    """;

    @Override
    public Optional<FaqDetailResponse> findById(Long faqId) {
        String sql = FIND_ALL_SQL + " WHERE f.id = ?";
        try {
            FaqDetailResponse faq = jdbcTemplate.queryForObject(sql, FAQ_MAPPER, faqId);
            return Optional.ofNullable(faq);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<FaqDetailResponse> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL + " ORDER BY f.created_at DESC", FAQ_MAPPER);
    }
    
    @Override
    public Optional<FaqCategoryResponse> findCategoryById(Integer categoryId) {
        String sql = "SELECT id, name FROM Faq_category WHERE id = ?";
        try {
            FaqCategoryResponse category = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new FaqCategoryResponse(rs.getInt("id"), rs.getString("name")), categoryId);
            return Optional.ofNullable(category);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
