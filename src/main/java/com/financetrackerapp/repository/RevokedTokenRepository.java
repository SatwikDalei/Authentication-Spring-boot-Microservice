package com.financetrackerapp.repository;

import com.financetrackerapp.entity.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, String> {
    boolean existsByToken(String token);
    void deleteByExpiryDateBefore(LocalDateTime now);
}
