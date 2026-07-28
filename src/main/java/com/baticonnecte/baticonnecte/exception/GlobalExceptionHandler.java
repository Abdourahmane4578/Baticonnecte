package com.baticonnecte.baticonnecte.exception;

import com.baticonnecte.baticonnecte.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // Erreurs de validation @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );


        return ResponseEntity
                .badRequest()
                .body(
                        ErrorResponseDto.builder()
                                .message(errors.toString())
                                .error("VALIDATION_ERROR")
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .build()
                );
    }



    // Exceptions personnalisées
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(
            CustomException ex
    ) {

        return ResponseEntity
                .status(ex.getStatus())
                .body(
                        ErrorResponseDto.builder()
                                .message(ex.getMessage())
                                .error(ex.getStatus().name())
                                .statusCode(ex.getStatus().value())
                                .build()
                );
    }



    // ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(
            ResponseStatusException ex
    ) {

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(
                        ErrorResponseDto.builder()
                                .message(ex.getReason())
                                .error(ex.getStatusCode().toString())
                                .statusCode(ex.getStatusCode().value())
                                .build()
                );
    }



    // Authentification
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(
            BadCredentialsException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorResponseDto.builder()
                                .message("Email ou mot de passe incorrect")
                                .error("UNAUTHORIZED")
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .build()
                );
    }



    // Autorisation
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(
            AccessDeniedException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        ErrorResponseDto.builder()
                                .message("Vous n'avez pas les permissions nécessaires")
                                .error("FORBIDDEN")
                                .statusCode(HttpStatus.FORBIDDEN.value())
                                .build()
                );
    }



    // Erreurs générales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
            Exception ex
    ) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponseDto.builder()
                                .message(ex.getMessage())
                                .error("INTERNAL_SERVER_ERROR")
                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .build()
                );
    }
}