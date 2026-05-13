package com.ciclocare.repository;

import com.ciclocare.entity.EnergiaDiaria;
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
public interface EnergiaDiariaRepository extends JpaRepository<EnergiaDiaria, UUID> {

    Optional<EnergiaDiaria> findByUsuarioAndData(Usuario usuario, LocalDate data);

    @Query("SELECT e FROM EnergiaDiaria e WHERE e.usuario = :usuario AND e.data >= :dataInicio ORDER BY e.data DESC")
    List<EnergiaDiaria> findHistorico(@Param("usuario") Usuario usuario, @Param("dataInicio") LocalDate dataInicio, Pageable pageable);

    @Query("SELECT e FROM EnergiaDiaria e WHERE e.usuario = :usuario ORDER BY e.data DESC")
    List<EnergiaDiaria> findAllByUsuario(@Param("usuario") Usuario usuario);
}
