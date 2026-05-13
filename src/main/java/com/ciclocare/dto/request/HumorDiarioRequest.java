package com.ciclocare.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class HumorDiarioRequest {

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @NotBlank(message = "Humor é obrigatório")
    private String humor;

    private String notas;
}
