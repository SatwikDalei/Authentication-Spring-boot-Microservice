package com.financetrackerapp.service;


import com.financetrackerapp.entity.UserEntity;
import com.financetrackerapp.repository.AuthenticatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AuthenticatorRepository authenticatorRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserEntity> userEntityList = authenticatorRepository.findByUsername(username);
        if(userEntityList==null || userEntityList.isEmpty()) throw new UsernameNotFoundException(username);
        else{
            UserEntity userEntity= userEntityList.get(0);

            User user = new User(userEntity.getUsername(),userEntity.getPassword(),List.of());
            return user;
        }
    }
}
