package com.opom.bankingapp.repository;

import com.opom.bankingapp.dto.admin.AdminApprovalDetails;
import com.opom.bankingapp.dto.admin.UserAdminResponse;
import com.opom.bankingapp.dto.auth.RegisterPersonalDetailsRequest;
import com.opom.bankingapp.dto.user.ChangeUserDetailRequest;
import com.opom.bankingapp.dto.user.ProfileDetailsDto;
import com.opom.bankingapp.dto.user.UpdateProfileRequest;
import com.opom.bankingapp.model.UserPrincipal;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<UserPrincipal> findByUsername(String username);

    Optional<UserPrincipal> findByEmail(String email);

    boolean existsByEmail(String email);

    long saveProfileDetail(RegisterPersonalDetailsRequest request);

    long saveUser(String username, String email, String hashedPassword, long profileId, int roleId, int statusId);

    void saveKyc(String kycType, String kycData, long profileId);

    int getRoleId(String roleType);

    void updatePin(Long userId, String hashedPin);
    void updatePolicyAgreement(Long userId, boolean agreement);
    Optional<String> findFullNameByUserId(Long userId);
    void updateAutoSaveReceipt(Long userId, boolean flag);
    Optional<String> findHashedPasswordById(Long userId);
    Optional<String> findHashedPinById(Long userId);
    void updatePassword(Long userId, String newHashedPassword);
    void updateSelectedAccount(Long userId, int accountId);
    Optional<Long> findSelectedAccountIdByUserId(Long userId);

    void updateStatus(Long userId, int statusId);

    Optional<AdminApprovalDetails> findApprovalDetailsById(Long userId);

    List<UserAdminResponse> findAllUsersForAdmin();
    
    void changeUserDetails(Long userId, ChangeUserDetailRequest request);

    Optional<ProfileDetailsDto> findProfileDetailsByUserId(Long userId);

    void updateProfileDetails(Long userId, UpdateProfileRequest request);
}
