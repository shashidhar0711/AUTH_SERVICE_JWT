package com.mslearning.AUTH_SERVICE_JWT.controllers;

import com.mslearning.AUTH_SERVICE_JWT.dtos.*;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.InvalidRequestException;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.SessionNotFoundException;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.UserLoggedOutException;
import com.mslearning.AUTH_SERVICE_JWT.services.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign_up")
    public ResponseEntity<SignUpResponseDto> singup(SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto response = new SignUpResponseDto();
        try {
            if(this.authService.singUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword())) {
                response.setRequestStatus(RequestStatus.SUCCESS);
            } else {
                response.setRequestStatus(RequestStatus.FAILURE);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        try {
            String token = authService.login(request.getEmail(), request.getPassword());
            LoginResponseDto loginDto = new LoginResponseDto();
            loginDto.setRequestStatus(RequestStatus.SUCCESS);
//            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpHeaders headers = new HttpHeaders();
            headers.add("AUTH_TOKEN", token);

            return ResponseEntity.ok().headers(headers).body(loginDto);
        } catch (Exception e) {
            LoginResponseDto loginDto = new LoginResponseDto();
            loginDto.setRequestStatus(RequestStatus.FAILURE);
            return ResponseEntity.badRequest().body(loginDto);
        }
    }

    @GetMapping("/validate")
    public JwtUserDto validate(@RequestParam("token") String token) throws InvalidRequestException, UserLoggedOutException {
        System.out.println("Here I am");
        return authService.validate(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(
            @RequestHeader("AUTH_TOKEN") String token) throws UserLoggedOutException, SessionNotFoundException {

        this.authService.logout(token);

        LogoutResponseDto response = new LogoutResponseDto();
        response.setRequestStatus(RequestStatus.SUCCESS);
        response.setMessage("Logged out successfully.");

        return ResponseEntity.ok(response);
    }
}
