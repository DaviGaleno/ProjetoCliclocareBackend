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
public class EnergiaDiariaResponse {

    private UUID id;
    private UUID usuarioId;
    private LocalDate data;
    private Integer nivelEnergia;
    private String notas;
    private LocalDateTime criadoEm;
}
