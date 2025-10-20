package com.opom.bankingapp.service;

import java.util.Optional;

public interface TokenService {

    String encode(Object payload, long expirationMs);

    <T> Optional<T> decode(String token, Class<T> payloadType);
}
