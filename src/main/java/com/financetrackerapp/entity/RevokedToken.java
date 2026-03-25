package com.financetrackerapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="revoked_tokens")
public class RevokedToken {

    @Id
    private String token; // store JWT string

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    public RevokedToken() {}

    public RevokedToken(String token, LocalDateTime expiryDate) {
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
