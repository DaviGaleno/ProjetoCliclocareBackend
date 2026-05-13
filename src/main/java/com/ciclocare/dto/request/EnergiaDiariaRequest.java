package com.ciclocare.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergiaDiariaRequest {

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @NotNull(message = "Nível de energia é obrigatório")
    @Min(value = 1, message = "Nível de energia deve ser entre 1 e 10")
    @Max(value = 10, message = "Nível de energia deve ser entre 1 e 10")
    private Integer nivelEnergia;

    private String notas;
}
