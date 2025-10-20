package com.opom.bankingapp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opom.bankingapp.service.JwtService;
import com.opom.bankingapp.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public TokenServiceImpl(final JwtService jwtService, final  ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public String encode(final Object payload, final  long expirationMs) {
        try {
            final String jsonPayload = objectMapper.writeValueAsString(payload);
            return jwtService.generateToken(jsonPayload, expirationMs);
        } catch (Exception e) {
            logger.error("Failed to serialize token payload", e);
            throw new RuntimeException("Error encoding token", e);
        }
    }

    @Override
    public <T> Optional<T> decode(final String token, final  Class<T> payloadType) {
        try {
            final String jsonPayload = jwtService.extractClaim(token, Claims::getSubject);
            final T payload = objectMapper.readValue(jsonPayload, payloadType);
            return Optional.of(payload);
        } catch (JwtException e) {
            logger.warn("Invalid JWT: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to deserialize token payload", e);
        }
        return Optional.empty();
    }
}
