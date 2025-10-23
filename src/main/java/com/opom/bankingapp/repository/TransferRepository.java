package com.opom.bankingapp.repository;

import com.opom.bankingapp.dto.transfer.TransferPrepareResponse;
import java.util.Optional;

public interface TransferRepository {
    Optional<TransferPrepareResponse> findNicknamePrepareDetails(Long nicknameId, Long fromUserId);
    Optional<TransferPrepareResponse> findAccountPrepareDetails(String accountNumber);
}
