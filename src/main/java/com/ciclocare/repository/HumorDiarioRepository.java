package com.ciclocare.repository;

import com.ciclocare.entity.HumorDiario;
import com.ciclocare.entity.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HumorDiarioRepository extends JpaRepository<HumorDiario, UUID> {

    Optional<HumorDiario> findByUsuarioAndData(Usuario usuario, LocalDate data);

    @Query("SELECT h FROM HumorDiario h WHERE h.usuario = :usuario AND h.data >= :dataInicio ORDER BY h.data DESC")
    List<HumorDiario> findHistorico(@Param("usuario") Usuario usuario, @Param("dataInicio") LocalDate dataInicio, Pageable pageable);

    @Query("SELECT h FROM HumorDiario h WHERE h.usuario = :usuario ORDER BY h.data DESC")
    List<HumorDiario> findAllByUsuario(@Param("usuario") Usuario usuario);
}
