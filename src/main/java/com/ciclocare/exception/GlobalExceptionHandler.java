package com.ciclocare.exception;

import com.ciclocare.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request) {
        ApiResponse apiResponse = ApiResponse.erro(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse> handleValidation(
            ValidationException ex,
            WebRequest request) {
        ApiResponse apiResponse = ApiResponse.erro(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(com.ciclocare.exception.AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthentication(
            com.ciclocare.exception.AuthenticationException ex,
            WebRequest request) {
        ApiResponse apiResponse = ApiResponse.erro(ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(
            AuthenticationException ex,
            WebRequest request) {
        ApiResponse apiResponse = ApiResponse.erro("Falha na autenticação");
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nomeField = ((FieldError) error).getField();
            String mensagemErro = error.getDefaultMessage();
            erros.put(nomeField, mensagemErro);
        });
        ApiResponse apiResponse = ApiResponse.builder()
                .sucesso(false)
                .mensagem("Erro de validação")
                .dados(erros)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(
            Exception ex,
            WebRequest request) {
        ApiResponse apiResponse = ApiResponse.erro("Erro interno do servidor");
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
