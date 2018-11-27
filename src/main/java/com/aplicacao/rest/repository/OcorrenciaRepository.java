package com.aplicacao.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicacao.rest.domain.Ocorrencia;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia,Integer>{

}
