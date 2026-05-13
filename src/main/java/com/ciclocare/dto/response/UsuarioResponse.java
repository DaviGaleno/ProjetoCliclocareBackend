package com.ciclocare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private UUID id;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;
    private LocalDateTime ultimaAtualizacao;
    private Boolean ativo;
}
