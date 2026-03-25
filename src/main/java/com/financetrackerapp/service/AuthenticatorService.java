package com.financetrackerapp.service;

import com.financetrackerapp.dto.AuthResponseDto;
import com.financetrackerapp.dto.UserDto;

public interface AuthenticatorService {

    public AuthResponseDto authenticate(UserDto userDto);

    public UserDto register(UserDto userDto);
    public UserDto getUserInfo(String authToken);
    public AuthResponseDto refreshAccessToken(String refreshToken);
    public void logout(String authHeader);

}
