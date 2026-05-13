package com.ciclocare.controller;

import com.ciclocare.dto.request.EnergiaDiariaRequest;
import com.ciclocare.dto.response.ApiResponse;
import com.ciclocare.service.EnergiaDiariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/energias")
@RequiredArgsConstructor
public class EnergiaDiariaController {

    private final EnergiaDiariaService energiaService;

    @PostMapping
    public ResponseEntity<ApiResponse> registrar(@Valid @RequestBody EnergiaDiariaRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UUID usuarioId = UUID.randomUUID(); // TODO: Extrair do JWT
            var energia = energiaService.registrar(usuarioId, request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.sucesso("Energia registrada com sucesso", energia));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> buscarPorId(@PathVariable UUID id) {
        try {
            var energia = energiaService.buscarPorId(id);
            return ResponseEntity.ok(ApiResponse.sucesso("Energia encontrada", energia));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @GetMapping("/historico")
    public ResponseEntity<ApiResponse> buscarHistorico(
            @RequestParam(defaultValue = "30") Integer dias) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UUID usuarioId = UUID.randomUUID(); // TODO: Extrair do JWT
            var historico = energiaService.buscarHistorico(usuarioId, dias);
            return ResponseEntity.ok(ApiResponse.sucesso("Histórico de energia", historico));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EnergiaDiariaRequest request) {
        try {
            var energiaAtualizada = energiaService.atualizar(id, request);
            return ResponseEntity.ok(ApiResponse.sucesso("Energia atualizada com sucesso", energiaAtualizada));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletar(@PathVariable UUID id) {
        try {
            energiaService.deletar(id);
            return ResponseEntity.ok(ApiResponse.sucesso("Energia deletada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }
}
