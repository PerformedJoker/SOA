package com.aplicacao.rest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicacao.rest.list.Bairro;

public interface BairroRepository extends JpaRepository<Bairro,Integer>{
	
	Bairro findByDescricao(String descricao);

}
