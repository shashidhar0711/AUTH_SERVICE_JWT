package com.mslearning.AUTH_SERVICE_JWT.services;

import com.mslearning.AUTH_SERVICE_JWT.dtos.JwtUserDto;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.UserAlreadyExistException;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.UserNotFoundException;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.WrongPasswordException;
import com.mslearning.AUTH_SERVICE_JWT.models.Role;
import com.mslearning.AUTH_SERVICE_JWT.models.User;
import com.mslearning.AUTH_SERVICE_JWT.repositories.RoleRepository;
import com.mslearning.AUTH_SERVICE_JWT.repositories.UserRepository;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
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
    private RoleRepository roleRepository;
//    private SecretKey key = Jwts.SIG.HS256.key().build();

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private Integer expirationDays;

    private SecretKey key;

//    private SecretKey key = Keys.hmacShaKeyFor("MyVeryStrongSecretKeyForJWT123456".getBytes(StandardCharsets.UTF_8));

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean singUp(String email, String password) throws UserAlreadyExistException {
        Optional<User> byEmail = this.userRepository.findByEmail(email);
        if(byEmail.isPresent()) {
            throw new UserAlreadyExistException("User with email: "+ email + "already exits");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        Role role = roleRepository.findByName("CUSTOMER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("CUSTOMER");
                    return roleRepository.save(r);
                });

        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(role);
        user.setRoles(rolesSet);

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
        Set<Role> roles = userOptional.get().getRoles();
        if(matches) {
            String token = creatJwtToken(userOptional.get().getId(),
                           roles,
                           userOptional.get().getEmail());

            return token;
        } else {
            throw new WrongPasswordException("Wrong password");
        }
    }

    public JwtUserDto validate(String token) {

        try {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        JwtUserDto response = new JwtUserDto();

        Date expiration = claimsJws.getPayload().getExpiration();
        Long userId = claimsJws.getPayload().get("user_id", Long.class);
        String email = claimsJws.getPayload().get("email", String.class);
        List<Role> roles = claimsJws.getPayload().get("roles", List.class);
        response.setRole(new HashSet<>(roles));
        response.setUserId(userId);
        response.setEmail(email);

        return response;
        } catch (Exception e) {
            return null;
        }
    }

    private String creatJwtToken(Long userId, Set<Role> roles, String email) {
        Map<String, Object> dataInJwt = new HashMap<>();
        dataInJwt.put("user_id", userId);

        List<String> roleNames = roles.stream().map(Role::getName).toList();
        dataInJwt.put("roles", roleNames);
        dataInJwt.put("email", email);

        Calendar instance = Calendar.getInstance();
        Date currentTime = instance.getTime();

        instance.add(Calendar.DAY_OF_MONTH, 30);
        Date datePlus30Days = instance.getTime();

        String token = Jwts.builder()
                .claims(dataInJwt)
                .expiration(datePlus30Days)
                .issuedAt(currentTime)
                .signWith(key)
                .compact();

        return token;
    }
}
