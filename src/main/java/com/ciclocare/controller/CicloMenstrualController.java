package com.ciclocare.controller;

import com.ciclocare.dto.request.CicloMenstrualRequest;
import com.ciclocare.dto.response.ApiResponse;
import com.ciclocare.service.CicloMenstrualService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ciclos")
@RequiredArgsConstructor
public class CicloMenstrualController {

    private final CicloMenstrualService cicloService;

    @PostMapping
    public ResponseEntity<ApiResponse> criar(@Valid @RequestBody CicloMenstrualRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Aqui você pode extrair o ID do usuário do token
            // Por enquanto, usando um mock
            UUID usuarioId = UUID.randomUUID(); // TODO: Extrair do JWT
            var ciclo = cicloService.criar(usuarioId, request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.sucesso("Ciclo registrado com sucesso", ciclo));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> buscarPorId(@PathVariable UUID id) {
        try {
            var ciclo = cicloService.buscarPorId(id);
            return ResponseEntity.ok(ApiResponse.sucesso("Ciclo encontrado", ciclo));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @GetMapping("/ultimo")
    public ResponseEntity<ApiResponse> buscarUltimo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UUID usuarioId = UUID.randomUUID(); // TODO: Extrair do JWT
            var ciclo = cicloService.buscarUltimo(usuarioId);
            return ResponseEntity.ok(ApiResponse.sucesso("Último ciclo", ciclo));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CicloMenstrualRequest request) {
        try {
            var cicloAtualizado = cicloService.atualizar(id, request);
            return ResponseEntity.ok(ApiResponse.sucesso("Ciclo atualizado com sucesso", cicloAtualizado));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletar(@PathVariable UUID id) {
        try {
            cicloService.deletar(id);
            return ResponseEntity.ok(ApiResponse.sucesso("Ciclo deletado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }
}
