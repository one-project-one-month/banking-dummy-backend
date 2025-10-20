package com.opom.bankingapp.repository;

import com.opom.bankingapp.dto.auth.RegisterPersonalDetailsRequest;
import com.opom.bankingapp.model.UserPrincipal;

import java.util.Optional;

public interface UserRepository {

    Optional<UserPrincipal> findByUsername(String username);

    Optional<UserPrincipal> findByEmail(String email);

    boolean existsByEmail(String email);

    long saveProfileDetail(RegisterPersonalDetailsRequest request);

    long saveUser(String username, String email, String hashedPassword, long profileId, int roleId);

    void saveKyc(String kycType, String kycData, long profileId);

    int getRoleId(String roleType);

    void updatePin(Long userId, String hashedPin);
    void updatePolicyAgreement(Long userId, boolean agreement);
    Optional<String> findFullNameByUserId(Long userId);
    void updateAutoSaveReceipt(Long userId, boolean flag);
    Optional<String> findHashedPasswordById(Long userId);
    Optional<String> findHashedPinById(Long userId);
    void updatePassword(Long userId, String newHashedPassword);
}
