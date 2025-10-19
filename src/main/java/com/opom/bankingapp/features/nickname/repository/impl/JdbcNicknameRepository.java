package com.opom.bankingapp.features.nickname.repository.impl;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.opom.bankingapp.features.nickname.dto.DetailResponse;
import com.opom.bankingapp.features.nickname.dto.NicknameListResponse;
import com.opom.bankingapp.features.nickname.dto.NicknameOptionResponse;
import com.opom.bankingapp.features.nickname.dto.NicknameRequest;
import com.opom.bankingapp.features.nickname.repository.NicknameRepository;

@Repository
public class JdbcNicknameRepository implements NicknameRepository {
	private final JdbcTemplate jdbcTemplate;
	public JdbcNicknameRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
    @Transactional
    public void saveNickname(Long userId, NicknameRequest request) {
	    String sql = """
	        INSERT INTO Nickname 
	        (from_account, to_account, nickname, created_at, created_by)
	        VALUES (?, ?, ?, NOW(), ?)
	    """;

	    jdbcTemplate.update(
	        sql,
	        userId,
	        request.toAccountId(),
	        request.nickname(),
	        userId
	    );
	}
	
	@Override
	public NicknameListResponse getNicknamesByUserId(Long userId) {
	    String sql = """
	        SELECT n.id AS nickname_id,
	    		   n.name AS nickname,
	               a.id AS account_id,
	               a.account_number
	          FROM Nickname n
	          JOIN Account_detail a ON n.to_account = a.id
	         WHERE n.from_account = ?
	    """;

	    List<NicknameOptionResponse> nicknameOptions = jdbcTemplate.query(
	    	    sql,
	    	    (rs, rowNum) -> {
	    	        int nicknameId = rs.getInt("nickname_id");
	    	        String nickname = rs.getString("nickname");
	    	        int accountId = rs.getInt("account_id");
	    	        String accountNumber = rs.getString("account_number");
	    	        DetailResponse accountDetail = new DetailResponse(accountId, accountNumber);
	    	        return new NicknameOptionResponse(nicknameId, nickname, accountDetail);
	    	    },
	    	    userId
	    	);


	    return new NicknameListResponse(nicknameOptions);
	}
	
	@Override
	@Transactional
	public void updateNickname(Long userId, Long nicknameId, NicknameRequest request) {
	    String sql = """
	        UPDATE Nickname
	           SET to_account = ?,
	               nickname = ?,
	               updated_at = NOW(),
	               updated_by = ?
	         WHERE id = ?
	           AND from_account = ?
	    """;

	    jdbcTemplate.update(
	        sql,
	        request.toAccountId(),
	        request.nickname(),
	        userId,   
	        nicknameId, 
	        userId       
	    );
	}

	@Override
	@Transactional
	public void deleteNickname(Long userId, Long nicknameId) {
	    String sql = """
	        DELETE FROM Nickname
	         WHERE id = ?
	           AND from_account = ?
	    """;

	    jdbcTemplate.update(sql, nicknameId, userId);
	}

}
