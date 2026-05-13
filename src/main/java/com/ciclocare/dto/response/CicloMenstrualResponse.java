package com.ciclocare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CicloMenstrualResponse {

    private UUID id;
    private UUID usuarioId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer duracaoCiclo;
    private Integer duracaoMenstruacao;
    private LocalDate ultimaMenstruacao;
    private LocalDate proximaPrevisao;
    private String intensidadeFluxo;
    private LocalDateTime criadoEm;
}
