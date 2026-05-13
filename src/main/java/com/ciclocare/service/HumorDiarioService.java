package com.ciclocare.service;

import com.ciclocare.dto.request.HumorDiarioRequest;
import com.ciclocare.dto.response.HumorDiarioResponse;
import com.ciclocare.entity.HumorDiario;
import com.ciclocare.entity.Usuario;
import com.ciclocare.exception.ResourceNotFoundException;
import com.ciclocare.repository.HumorDiarioRepository;
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
public class HumorDiarioService {

    private final HumorDiarioRepository humorRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public HumorDiarioResponse registrar(UUID usuarioId, HumorDiarioRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(
                usuarioService.buscarPorId(usuarioId).getEmail()
        );

        HumorDiario humor = HumorDiario.builder()
                .usuario(usuario)
                .data(request.getData())
                .humor(request.getHumor())
                .notas(request.getNotas())
                .build();

        HumorDiario humorSalvo = humorRepository.save(humor);
        return mapToResponse(humorSalvo);
    }

    public HumorDiarioResponse buscarPorId(UUID id) {
        HumorDiario humor = humorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de humor não encontrado"));
        return mapToResponse(humor);
    }

    public List<HumorDiarioResponse> buscarHistorico(UUID usuarioId, Integer dias) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        LocalDate dataInicio = LocalDate.now().minusDays(dias != null ? dias : 30);
        Pageable pageable = PageRequest.of(0, 1000);
        return humorRepository.findHistorico(usuario, dataInicio, pageable)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<HumorDiarioResponse> buscarTodosPorUsuario(UUID usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        return humorRepository.findAllByUsuario(usuario)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HumorDiarioResponse atualizar(UUID id, HumorDiarioRequest request) {
        HumorDiario humor = humorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de humor não encontrado"));

        humor.setData(request.getData());
        humor.setHumor(request.getHumor());
        humor.setNotas(request.getNotas());

        HumorDiario humorAtualizado = humorRepository.save(humor);
        return mapToResponse(humorAtualizado);
    }

    @Transactional
    public void deletar(UUID id) {
        HumorDiario humor = humorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de humor não encontrado"));
        humorRepository.delete(humor);
    }

    private HumorDiarioResponse mapToResponse(HumorDiario humor) {
        return HumorDiarioResponse.builder()
                .id(humor.getId())
                .usuarioId(humor.getUsuario().getId())
                .data(humor.getData())
                .humor(humor.getHumor())
                .notas(humor.getNotas())
                .criadoEm(humor.getCriadoEm())
                .build();
    }
}
