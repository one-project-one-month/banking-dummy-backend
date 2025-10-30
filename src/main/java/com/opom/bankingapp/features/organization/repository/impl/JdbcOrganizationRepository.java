package com.opom.bankingapp.features.organization.repository.impl;

import com.opom.bankingapp.dto.organization.OrganizationRequest;
import com.opom.bankingapp.dto.organization.OrganizationResponse;
import com.opom.bankingapp.exception.ResourceNotFoundException;
import com.opom.bankingapp.features.organization.repository.OrganizationRepository;
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
public class JdbcOrganizationRepository implements OrganizationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrganizationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class OrganizationResponseRowMapper implements RowMapper<OrganizationResponse> {
        @Override
        public OrganizationResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            String organizationAdmin = rs.getString("admin_fullname");
            String adminEmail = rs.getString("admin_email");

            return new OrganizationResponse(
                    rs.getLong("id"),
                    rs.getString("name"),
                    organizationAdmin != null ? organizationAdmin : "N/A", 
                    adminEmail != null ? adminEmail : "N/A", 
                    true,
                    rs.getTimestamp("created_at").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    Optional.ofNullable(rs.getTimestamp("updated_at"))
                            .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                            .orElse(null),
                    rs.getLong("created_by"),
                    rs.getLong("updated_by")
            );
        }
    }

    private static final OrganizationResponseRowMapper ORGANIZATION_MAPPER = new OrganizationResponseRowMapper();

    private static final String ORGANIZATION_FIND_ALL_QUERY = """
        WITH OrganizationAdmin AS (
            SELECT
                pd.organization_id,
                pd.fullname AS admin_fullname,
                u.email AS admin_email,
                ROW_NUMBER() OVER (PARTITION BY pd.organization_id ORDER BY u.id) as rn
            FROM Profile_detail pd
            JOIN Users u ON u.profile_id = pd.id
            WHERE pd.organization_id IS NOT NULL
        )
        SELECT 
            o.id, o.name, o.shortcode, o.address, o.country, o.created_at, o.updated_at, o.created_by, o.updated_by,
            oa.admin_fullname, oa.admin_email
        FROM Organization o
        LEFT JOIN OrganizationAdmin oa ON o.id = oa.organization_id AND oa.rn = 1
    """;


    @Override
    public Long save(Long createdBy, OrganizationRequest request) {
        String sql = """
            INSERT INTO Organization (name, shortcode, address, country, created_by)
            VALUES (?, ?, ?, ?, ?)
        """;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.name());
            ps.setString(2, request.shortcode());
            ps.setString(3, request.address());
            ps.setString(4, request.country());
            ps.setLong(5, createdBy);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Long organizationId, Long updatedBy, OrganizationRequest request) {
        String sql = """
            UPDATE Organization
            SET name = ?,
                shortcode = ?,
                address = ?,
                country = ?,
                updated_at = NOW(),
                updated_by = ?
            WHERE id = ?
        """;
        int updated = jdbcTemplate.update(sql,
            request.name(),
            request.shortcode(),
            request.address(),
            request.country(),
            updatedBy,
            organizationId
        );
        if (updated == 0) {
             throw new ResourceNotFoundException("Organization not found with ID: " + organizationId);
        }
    }

    @Override
    public void delete(Long organizationId) {
        String sql = "DELETE FROM Organization WHERE id = ?";
        jdbcTemplate.update(sql, organizationId);
    }

    @Override
    public Optional<OrganizationResponse> findById(Long organizationId) {
        String sql = ORGANIZATION_FIND_ALL_QUERY + " WHERE o.id = ?";
        try {
            OrganizationResponse organization = jdbcTemplate.queryForObject(sql, ORGANIZATION_MAPPER, organizationId);
            return Optional.ofNullable(organization);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrganizationResponse> findAll() {
        return jdbcTemplate.query(ORGANIZATION_FIND_ALL_QUERY + " ORDER BY o.created_at DESC", ORGANIZATION_MAPPER);
    }
}
