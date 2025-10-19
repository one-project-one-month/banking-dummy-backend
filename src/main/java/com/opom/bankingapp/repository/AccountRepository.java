package com.opom.bankingapp.repository;

import java.util.Optional;

public interface AccountRepository {
    Optional<Double> findBalanceByUserId(long userId);
}
