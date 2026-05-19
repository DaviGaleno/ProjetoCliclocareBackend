package com.ciclocare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
    private String senha;

	@NotNull(message = "Data de nascimento é obrigatória")
	private LocalDate nascimento;

	@NotNull(message = "Duração do ciclo é obrigatória")
	private Integer duracaoCiclo;

	@NotNull(message = "Duração da menstruação é obrigatória")
	private Integer duracaoMenstruacao;

	@NotNull(message = "Última menstruação é obrigatória")
	private LocalDate ultimaMenstruacao;
}
