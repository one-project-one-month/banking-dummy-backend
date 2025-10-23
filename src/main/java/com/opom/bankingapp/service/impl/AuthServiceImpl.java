package com.opom.bankingapp.service.impl;

import com.opom.bankingapp.dto.auth.AuthResponse;
import com.opom.bankingapp.dto.auth.LoginRequest;
import com.opom.bankingapp.dto.auth.PersonalDetailsTemplateResponse;
import com.opom.bankingapp.dto.auth.RegisterPersonalDetailsRequest;
import com.opom.bankingapp.dto.common.OptionDto;
import com.opom.bankingapp.exception.EmailAlreadyExistsException;
import com.opom.bankingapp.model.UserPrincipal;
import com.opom.bankingapp.repository.AccountRepository;
import com.opom.bankingapp.repository.CommonRepository;
import com.opom.bankingapp.repository.UserRepository;
import com.opom.bankingapp.service.AuthService;
import com.opom.bankingapp.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final CommonRepository commonRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(CommonRepository commonRepository,
                           UserRepository userRepository,
                           AccountRepository accountRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager) {
        this.commonRepository = commonRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public PersonalDetailsTemplateResponse getPersonalDetailsTemplate() {
        List<OptionDto> genders = commonRepository.getGenderOptions();
        List<OptionDto> nationalities = commonRepository.getNationalityOptions();
        return new PersonalDetailsTemplateResponse(genders, nationalities);
    }

    @Override
    @Transactional
    public AuthResponse registerPersonalDetails(RegisterPersonalDetailsRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email is already taken: " + request.email());
        }

        long profileId = userRepository.saveProfileDetail(request);

        userRepository.saveKyc(request.kycType(), request.kycData(), profileId);

        String username = request.email().split("@")[0];
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        int roleId = userRepository.getRoleId("CUSTOMER"); 

        userRepository.saveUser(username, request.email(), hashedPassword, profileId, roleId);

        UserPrincipal userPrincipal = new UserPrincipal(-1L, username, hashedPassword, "CUSTOMER", request.email());
        String jwtToken = jwtService.generateToken(userPrincipal);
        
        System.out.println("---- DEMO: User created ----");
        System.out.println("Username: " + username);
        System.out.println("Password: " + rawPassword + " (This is a demo password)");
        System.out.println("-----------------------------");

        return new AuthResponse(jwtToken, null, request.email(), username, 0.0);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userPrincipal);

        double balance = accountRepository.findBalanceByUserId(userPrincipal.getId())
                .orElse(0.0);

        return new AuthResponse(
                jwtToken,
                null,
                userPrincipal.getEmail(),
                userPrincipal.getUsername(),
                balance
        );
    }

    @Override
    public AuthResponse refreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid refresh token request");
        }

        final String refreshToken = authHeader.substring(7);
        final String username;
        try {
            username = jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        if (username != null) {
            UserPrincipal userPrincipal = this.userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (jwtService.isTokenValid(refreshToken, userPrincipal)) {
                String newAccessToken = jwtService.generateToken(userPrincipal);

                double balance = accountRepository.findBalanceByUserId(userPrincipal.getId())
                        .orElse(0.0);

                return new AuthResponse(
                        newAccessToken,
                        refreshToken,
                        userPrincipal.getEmail(),
                        userPrincipal.getUsername(),
                        balance
                );
            }
        }
        throw new BadCredentialsException("Invalid refresh token");
    }
}
