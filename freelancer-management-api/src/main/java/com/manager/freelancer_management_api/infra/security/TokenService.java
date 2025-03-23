package com.manager.freelancer_management_api.infra.security;

import com.manager.freelancer_management_api.domain.repositories.UserRepository;
import com.manager.freelancer_management_api.domain.user.dtos.LoginDTO;
import com.manager.freelancer_management_api.domain.user.entities.User;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;


    public TokenService(UserRepository userRepository, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String  generateToken(LoginDTO login){
        User user = (User) userRepository.findByEmail(login.email());

        var now = Instant.now();
        var expirationTime = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("Freelancer management api")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationTime))
                .subject(String.valueOf(user.getId()))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String decodeToken(String token){
        return jwtDecoder.decode(token).getSubject();
    }

    public String isTokenValid(String token){
        try{
            decodeToken(token);
            return "valid";
        } catch (Exception e){
            return "Invalid token.";
        }
    }
}
