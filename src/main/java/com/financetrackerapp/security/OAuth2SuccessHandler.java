package com.financetrackerapp.security;

import com.financetrackerapp.entity.UserEntity;
import com.financetrackerapp.repository.AuthenticatorRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticatorRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // GitHub may not send email directly
        if (email == null) {
            email = oAuth2User.getAttribute("login") + "@github.com";
        }

        // Check user exists
        List<UserEntity> users = userRepository.findByUsername(email);

        UserEntity user;

        if (users.isEmpty()) {
            user = new UserEntity();
            user.setUsername(email);
            user.setEmail(email);
            user.setFirstName(name);
            user.setPassword(passwordEncoder.encode("oauth_dummy"));

            userRepository.save(user);
        } else {
            user = users.get(0);
        }

        // Generate JWT
        String accessToken = jwtUtils.generateToken(user.getUsername());

        // Send token in response
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"accessToken\": \"" + accessToken + "\"}"
        );
    }
}
