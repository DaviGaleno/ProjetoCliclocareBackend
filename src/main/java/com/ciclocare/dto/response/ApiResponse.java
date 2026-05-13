package com.ciclocare.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private Boolean sucesso;
    private String mensagem;
    private Object dados;
    private LocalDateTime timestamp;

    public static ApiResponse sucesso(String mensagem, Object dados) {
        return ApiResponse.builder()
                .sucesso(true)
                .mensagem(mensagem)
                .dados(dados)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse sucesso(String mensagem) {
        return ApiResponse.builder()
                .sucesso(true)
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse erro(String mensagem) {
        return ApiResponse.builder()
                .sucesso(false)
                .mensagem(mensagem)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
