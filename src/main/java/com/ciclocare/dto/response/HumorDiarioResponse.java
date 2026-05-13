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
public class HumorDiarioResponse {

    private UUID id;
    private UUID usuarioId;
    private LocalDate data;
    private String humor;
    private String notas;
    private LocalDateTime criadoEm;
}
