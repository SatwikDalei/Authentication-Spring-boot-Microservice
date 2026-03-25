package com.financetrackerapp.repository;

import com.financetrackerapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthenticatorRepository extends JpaRepository<UserEntity,Integer> {
    public List<UserEntity> findByUsername(String userName);
}
