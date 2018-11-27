package com.aplicacao.rest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicacao.rest.domain.Ocorrencia;
import com.aplicacao.rest.list.Bairro;
import com.aplicacao.rest.list.Natureza;

public interface NaturezaRepository extends JpaRepository<Natureza,Integer>{
	Natureza findByDescricao(String descricao);
}
