package com.opom.bankingapp.features.nickname.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opom.bankingapp.features.nickname.dto.NicknameListResponse;
import com.opom.bankingapp.features.nickname.dto.NicknameRequest;
import com.opom.bankingapp.features.nickname.repository.NicknameRepository;
import com.opom.bankingapp.features.nickname.service.NicknameService;

@Service
public class NicknameServiceImpl implements NicknameService {
	private final NicknameRepository nicknameRepository;

	public NicknameServiceImpl(NicknameRepository nicknameRepository) {
		this.nicknameRepository = nicknameRepository;
	}

	@Override
	@Transactional
	public void saveNickname(Long userId, NicknameRequest request) {
		nicknameRepository.saveNickname(userId, request);
	}

	@Override
	public NicknameListResponse getNicknamesByUserId(Long userId) {
		return nicknameRepository.getNicknamesByUserId(userId);
	}

	@Override
	@Transactional
	public void updateNickname(Long userId, Long nicknameId, NicknameRequest request) {
		nicknameRepository.updateNickname(userId, nicknameId, request);
	}

	@Override
	@Transactional
	public void deleteNickname(Long userId, Long nicknameId) {
		nicknameRepository.deleteNickname(userId, nicknameId);
	}
}
