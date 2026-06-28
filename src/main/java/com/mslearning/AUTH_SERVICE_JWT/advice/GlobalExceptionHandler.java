package com.mslearning.AUTH_SERVICE_JWT.advice;

import com.mslearning.AUTH_SERVICE_JWT.dtos.ExceptionResponseDto;
import com.mslearning.AUTH_SERVICE_JWT.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserNotFound(
            UserNotFoundException exception) {

        ExceptionResponseDto dto = new ExceptionResponseDto();
        dto.setMessage(exception.getMessage());
        dto.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionResponseDto> handleUserAlreadyExist(
            UserAlreadyExistException exception) {

        ExceptionResponseDto dto = new ExceptionResponseDto();
        dto.setMessage(exception.getMessage());
        dto.setStatus(HttpStatus.CONFLICT);

        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ExceptionResponseDto> handleWrongPassword(
            WrongPasswordException exception) {

        ExceptionResponseDto dto = new ExceptionResponseDto();
        dto.setMessage(exception.getMessage());
        dto.setStatus(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleWrongPassword(
            SessionNotFoundException exception) {

        ExceptionResponseDto dto = new ExceptionResponseDto();
        dto.setMessage(exception.getMessage());
        dto.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserLoggedOutException.class)
    public ResponseEntity<ExceptionResponseDto> handleWrongPassword(
            UserLoggedOutException exception) {

        ExceptionResponseDto dto = new ExceptionResponseDto();
        dto.setMessage(exception.getMessage());
        dto.setStatus(HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ExceptionResponseDto> handleInvalidRequest(
            InvalidRequestException exception) {

        ExceptionResponseDto dto = new ExceptionResponseDto();
        dto.setMessage(exception.getMessage());
        dto.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleGenericException(
            Exception exception) {

        ExceptionResponseDto dto = new ExceptionResponseDto();
        dto.setMessage(exception.getMessage());
        dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
