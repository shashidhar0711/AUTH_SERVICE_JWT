package com.mslearning.AUTH_SERVICE_JWT.services;

import com.mslearning.AUTH_SERVICE_JWT.exceptions.UserAlreadyExistException;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.UserNotFoundException;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.WrongPasswordException;
import com.mslearning.AUTH_SERVICE_JWT.models.User;
import com.mslearning.AUTH_SERVICE_JWT.repositories.UserRepository;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    private SecretKey key = Jwts.SIG.HS256.key().build();
    private SecretKey key = Keys.hmacShaKeyFor("MyVeryStrongSecretKeyForJWT123456".getBytes(StandardCharsets.UTF_8));

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean singUp(String email, String password) throws UserAlreadyExistException {
        Optional<User> byEmail = this.userRepository.findByEmail(email);
        if(byEmail.isPresent()) {
            throw new UserAlreadyExistException("User with email: "+ email + "already exits");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        this.userRepository.save(user);

        return true;
    }

    public String login(String email, String password) throws UserNotFoundException, WrongPasswordException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("User with email:" + email + "already exits");
        }

        boolean matches = bCryptPasswordEncoder.matches(
                password,
                userOptional.get().getPassword()
        );
        if(matches) {
            String token = creatJwtToken(userOptional.get().getId(),
                        new ArrayList<>(),
                        userOptional.get().getEmail());

            return token;
        } else {
            throw new WrongPasswordException("Wrong password");
        }
    }

    public boolean validate(String token) {
        try {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        Date expiration = claimsJws.getPayload().getExpiration();
        Long userId = claimsJws.getPayload().get("user_id", Long.class);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String creatJwtToken(Long userId, ArrayList<String> roles, String email) {
        Map<String, Object> dataInJwt = new HashMap<>();
        dataInJwt.put("user_id", userId);
        dataInJwt.put("roles", roles);
        dataInJwt.put("email", email);

        Calendar instance = Calendar.getInstance();
        Date time = instance.getTime();

        instance.add(Calendar.DAY_OF_MONTH, 30);
        Date datePlus30Days = instance.getTime();

        String token = Jwts.builder()
                .claims(dataInJwt)
                .expiration(datePlus30Days)
                .issuedAt(new Date())
                .signWith(key)
                .compact();

        return token;
    }
}
