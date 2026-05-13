package com.ciclocare.service;

import com.ciclocare.dto.request.CicloMenstrualRequest;
import com.ciclocare.dto.response.CicloMenstrualResponse;
import com.ciclocare.entity.CicloMenstrual;
import com.ciclocare.entity.Usuario;
import com.ciclocare.exception.ResourceNotFoundException;
import com.ciclocare.repository.CicloMenstrualRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CicloMenstrualService {

    private final CicloMenstrualRepository cicloRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public CicloMenstrualResponse criar(UUID usuarioId, CicloMenstrualRequest request) {
        Usuario usuario = usuarioService.buscarPorEmail(
                usuarioService.buscarPorId(usuarioId).getEmail()
        );

        LocalDate proximaPrevisao = request.getUltimaMenstruacao()
                .plusDays(request.getDuracaoCiclo());

        CicloMenstrual ciclo = CicloMenstrual.builder()
                .usuario(usuario)
                .dataInicio(request.getDataInicio())
                .dataFim(request.getDataFim())
                .duracaoCiclo(request.getDuracaoCiclo())
                .duracaoMenstruacao(request.getDuracaoMenstruacao())
                .ultimaMenstruacao(request.getUltimaMenstruacao())
                .proximaPrevisao(proximaPrevisao)
                .intensidadeFluxo(request.getIntensidadeFluxo())
                .build();

        CicloMenstrual cicloSalvo = cicloRepository.save(ciclo);
        return mapToResponse(cicloSalvo);
    }

    public CicloMenstrualResponse buscarPorId(UUID id) {
        CicloMenstrual ciclo = cicloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo menstrual não encontrado"));
        return mapToResponse(ciclo);
    }

    public List<CicloMenstrualResponse> buscarTodosPorUsuario(UUID usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        return cicloRepository.findAllByUsuario(usuario)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CicloMenstrualResponse buscarUltimo(UUID usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        CicloMenstrual ciclo = cicloRepository.findUltimoByUsuario(usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum ciclo registrado"));
        return mapToResponse(ciclo);
    }

    @Transactional
    public CicloMenstrualResponse atualizar(UUID id, CicloMenstrualRequest request) {
        CicloMenstrual ciclo = cicloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo menstrual não encontrado"));

        ciclo.setDataInicio(request.getDataInicio());
        ciclo.setDataFim(request.getDataFim());
        ciclo.setDuracaoCiclo(request.getDuracaoCiclo());
        ciclo.setDuracaoMenstruacao(request.getDuracaoMenstruacao());
        ciclo.setUltimaMenstruacao(request.getUltimaMenstruacao());
        ciclo.setProximaPrevisao(request.getUltimaMenstruacao().plusDays(request.getDuracaoCiclo()));
        ciclo.setIntensidadeFluxo(request.getIntensidadeFluxo());

        CicloMenstrual cicloAtualizado = cicloRepository.save(ciclo);
        return mapToResponse(cicloAtualizado);
    }

    @Transactional
    public void deletar(UUID id) {
        CicloMenstrual ciclo = cicloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo menstrual não encontrado"));
        cicloRepository.delete(ciclo);
    }

    private CicloMenstrualResponse mapToResponse(CicloMenstrual ciclo) {
        return CicloMenstrualResponse.builder()
                .id(ciclo.getId())
                .usuarioId(ciclo.getUsuario().getId())
                .dataInicio(ciclo.getDataInicio())
                .dataFim(ciclo.getDataFim())
                .duracaoCiclo(ciclo.getDuracaoCiclo())
                .duracaoMenstruacao(ciclo.getDuracaoMenstruacao())
                .ultimaMenstruacao(ciclo.getUltimaMenstruacao())
                .proximaPrevisao(ciclo.getProximaPrevisao())
                .intensidadeFluxo(ciclo.getIntensidadeFluxo())
                .criadoEm(ciclo.getCriadoEm())
                .build();
    }
}
