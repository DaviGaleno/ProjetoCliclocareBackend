package com.ciclocare.service;

import com.ciclocare.dto.request.EnergiaDiariaRequest;
import com.ciclocare.dto.response.EnergiaDiariaResponse;
import com.ciclocare.entity.EnergiaDiaria;
import com.ciclocare.entity.Usuario;
import com.ciclocare.exception.ResourceNotFoundException;
import com.ciclocare.repository.EnergiaDiariaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnergiaDiariaService {

    private final EnergiaDiariaRepository energiaRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public EnergiaDiariaResponse registrar(UUID usuarioId, EnergiaDiariaRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(
                usuarioService.buscarPorId(usuarioId).getEmail()
        );

        EnergiaDiaria energia = EnergiaDiaria.builder()
                .usuario(usuario)
                .data(request.getData())
                .nivelEnergia(request.getNivelEnergia())
                .notas(request.getNotas())
                .build();

        EnergiaDiaria energiaSalva = energiaRepository.save(energia);
        return mapToResponse(energiaSalva);
    }

    public EnergiaDiariaResponse buscarPorId(UUID id) {
        EnergiaDiaria energia = energiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de energia não encontrado"));
        return mapToResponse(energia);
    }

    public List<EnergiaDiariaResponse> buscarHistorico(UUID usuarioId, Integer dias) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        LocalDate dataInicio = LocalDate.now().minusDays(dias != null ? dias : 30);
        Pageable pageable = PageRequest.of(0, 1000);
        return energiaRepository.findHistorico(usuario, dataInicio, pageable)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<EnergiaDiariaResponse> buscarTodosPorUsuario(UUID usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        return energiaRepository.findAllByUsuario(usuario)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnergiaDiariaResponse atualizar(UUID id, EnergiaDiariaRequest request) {
        EnergiaDiaria energia = energiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de energia não encontrado"));

        energia.setData(request.getData());
        energia.setNivelEnergia(request.getNivelEnergia());
        energia.setNotas(request.getNotas());

        EnergiaDiaria energiaAtualizada = energiaRepository.save(energia);
        return mapToResponse(energiaAtualizada);
    }

    @Transactional
    public void deletar(UUID id) {
        EnergiaDiaria energia = energiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de energia não encontrado"));
        energiaRepository.delete(energia);
    }

    private EnergiaDiariaResponse mapToResponse(EnergiaDiaria energia) {
        return EnergiaDiariaResponse.builder()
                .id(energia.getId())
                .usuarioId(energia.getUsuario().getId())
                .data(energia.getData())
                .nivelEnergia(energia.getNivelEnergia())
                .notas(energia.getNotas())
                .criadoEm(energia.getCriadoEm())
                .build();
    }
}
