package com.ciclocare.controller;

import com.ciclocare.dto.request.HumorDiarioRequest;
import com.ciclocare.dto.response.ApiResponse;
import com.ciclocare.service.HumorDiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/humores")
@RequiredArgsConstructor
public class HumorDiarioController {

    private final HumorDiarioService humorService;

    @PostMapping
    public ResponseEntity<ApiResponse> registrar(@Valid @RequestBody HumorDiarioRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UUID usuarioId = UUID.randomUUID(); // TODO: Extrair do JWT
            var humor = humorService.registrar(usuarioId, request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.sucesso("Humor registrado com sucesso", humor));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> buscarPorId(@PathVariable UUID id) {
        try {
            var humor = humorService.buscarPorId(id);
            return ResponseEntity.ok(ApiResponse.sucesso("Humor encontrado", humor));
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
            var historico = humorService.buscarHistorico(usuarioId, dias);
            return ResponseEntity.ok(ApiResponse.sucesso("Histórico de humor", historico));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody HumorDiarioRequest request) {
        try {
            var humorAtualizado = humorService.atualizar(id, request);
            return ResponseEntity.ok(ApiResponse.sucesso("Humor atualizado com sucesso", humorAtualizado));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletar(@PathVariable UUID id) {
        try {
            humorService.deletar(id);
            return ResponseEntity.ok(ApiResponse.sucesso("Humor deletado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }
}
