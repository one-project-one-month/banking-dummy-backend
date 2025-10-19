package com.opom.bankingapp.features.nickname.repository;

import com.opom.bankingapp.features.nickname.dto.NicknameListResponse;
import com.opom.bankingapp.features.nickname.dto.NicknameRequest;

public interface NicknameRepository {
	void saveNickname(Long userId, NicknameRequest request);
	NicknameListResponse getNicknamesByUserId(Long userId);
	void updateNickname(Long userId, Long nicknameId, NicknameRequest request);
	void deleteNickname(Long userId, Long nicknameId);
}
