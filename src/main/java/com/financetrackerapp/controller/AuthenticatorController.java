package com.financetrackerapp.controller;

import com.financetrackerapp.dto.AuthResponseDto;
import com.financetrackerapp.dto.EmailRequestDto;
import com.financetrackerapp.dto.UserDto;
import com.financetrackerapp.dto.VerifyOtpDto;
import com.financetrackerapp.service.AuthenticatorService;
import com.financetrackerapp.service.AuthenticatorServiceImpl;
import com.financetrackerapp.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthenticatorController {
    @Autowired
    AuthenticatorService authenticatorService;

    @Autowired
    OtpService otpService;


    @PostMapping(value = "/authenticate", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<AuthResponseDto> authenticate(@Valid @RequestBody UserDto userDto){
        AuthResponseDto authBody=authenticatorService.authenticate(userDto);
        return new ResponseEntity<AuthResponseDto>(authBody, HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto){
        UserDto registeredDto= authenticatorService.register(userDto);
        return new ResponseEntity<UserDto>(registeredDto,HttpStatus.CREATED);
    }

    @GetMapping(value = "/user", produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader("Authorization") String authToken){
        UserDto userDto= authenticatorService.getUserInfo(authToken);
        return new ResponseEntity<UserDto>(userDto,HttpStatus.OK);
    }


    @PostMapping(
            value = "/refresh",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AuthResponseDto> refreshToken(
            @RequestBody String refreshToken) {

        AuthResponseDto response =
                authenticatorService.refreshAccessToken(refreshToken.trim());

        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String authHeader) {

        authenticatorService.logout(authHeader);

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping(value="/send-otp", consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> sendOtp(@RequestBody EmailRequestDto request){

        otpService.generateAndSendOtp(request.getEmail());

        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping(value="/verify-otp",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDto request){

        otpService.verifyOtp(request.getEmail(), request.getOtp());

        return ResponseEntity.ok("OTP verified");
    }

    @PostMapping(value="/resend-otp",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE},produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<String> resendOtp(@RequestBody EmailRequestDto request) {
        otpService.generateAndSendOtp(request.getEmail());
        return ResponseEntity.ok("OTP resent successfully");
    }

}
