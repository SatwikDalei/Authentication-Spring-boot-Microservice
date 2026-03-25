package com.financetrackerapp.service;

import com.financetrackerapp.dto.AuthResponseDto;
import com.financetrackerapp.dto.UserDto;
import com.financetrackerapp.entity.EmailOtp;
import com.financetrackerapp.entity.RevokedToken;
import com.financetrackerapp.entity.UserEntity;
import com.financetrackerapp.entity.RefreshToken;
import com.financetrackerapp.exception.LoginFailedException;
import com.financetrackerapp.repository.EmailOtpRepository;
import com.financetrackerapp.repository.RefreshTokenRepository;
import com.financetrackerapp.repository.AuthenticatorRepository;
import com.financetrackerapp.repository.RevokedTokenRepository;
import com.financetrackerapp.security.JwtUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class AuthenticatorServiceImpl implements AuthenticatorService{
    @Autowired
    private AuthenticatorRepository authenticatorRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RevokedTokenRepository revokedTokenRepository;
    @Autowired
    private EmailOtpRepository emailOtpRepository;

    @Override
    public AuthResponseDto authenticate(UserDto userDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getUsername(),
                            userDto.getPassword()
                    )
            );

            String accessToken = jwtUtils.generateToken(userDto.getUsername());
            refreshTokenRepository.deleteByUsername(userDto.getUsername());
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setUsername(userDto.getUsername());
            refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

            refreshTokenRepository.save(refreshToken);

            return new AuthResponseDto(accessToken, refreshToken.getToken());

        } catch (AuthenticationException e) {
            throw new LoginFailedException();
        }
    }


    @Override
    public UserDto register(UserDto userDto) {

        EmailOtp emailOtp = emailOtpRepository
                .findTopByEmailOrderByExpiryTimeDesc(userDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Please verify email first"));

        if (!emailOtp.isVerified()) {
            throw new RuntimeException("Email not verified");
        }

        // Optional: ensure OTP not expired during registration
        if (emailOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired, verify again");
        }

        // Optional cleanup
        emailOtpRepository.delete(emailOtp);

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

        authenticatorRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserInfo(String authToken) {

        String jwtToken= authToken.substring(7);
        String userName= jwtUtils.extractUsername(jwtToken);
        List<UserEntity> userEntityList= authenticatorRepository.findByUsername(userName);
        UserEntity userEntity= userEntityList.get(0);
        UserDto userDto= modelMapper.map(userEntity, UserDto.class);
        return userDto;

    }
    @Override
    public AuthResponseDto refreshAccessToken(String refreshToken) {

        RefreshToken token = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }
        refreshTokenRepository.delete(token);

        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken(UUID.randomUUID().toString());
        newRefreshToken.setUsername(token.getUsername());
        newRefreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        refreshTokenRepository.save(newRefreshToken);

        String newAccessToken =
                jwtUtils.generateToken(token.getUsername());

        return new AuthResponseDto(newAccessToken, newRefreshToken.getToken());
    }
    @Override
    @Transactional
    public void logout(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String jwtToken = authHeader.substring(7);

        String username = jwtUtils.extractUsername(jwtToken);
        Date expiryDate = jwtUtils.extractExpiration(jwtToken);

        // 1️⃣ Revoke access token
        RevokedToken revokedToken = new RevokedToken(
                jwtToken,
                expiryDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        );
        revokedTokenRepository.save(revokedToken);

        // 2️⃣ Delete refresh tokens for user
        refreshTokenRepository.deleteByUsername(username);
    }



}
