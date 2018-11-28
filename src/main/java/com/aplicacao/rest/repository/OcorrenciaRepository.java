package com.aplicacao.rest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicacao.rest.domain.Ocorrencia;
import com.aplicacao.rest.list.Bairro;
import com.aplicacao.rest.list.Natureza;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia,Integer>{
	List<Ocorrencia> findByDataRegistroIsBetween(Date dataInicio, Date dataFim);
	List<Ocorrencia> findByNatureza(Natureza natureza);
	List<Ocorrencia> findByBairro(Bairro bairro);
	List<Ocorrencia> findByDataRegistroIsBetweenAndNatureza(Date dataInicio, Date dataFim, Natureza natureza);
	List<Ocorrencia> findByDataRegistroIsBetweenAndBairro(Date dataInicio, Date dataFim, Bairro bairro);
	List<Ocorrencia> findByDataRegistroIsBetweenAndBairroAndNatureza(Date dataInicio, Date dataFim, Bairro bairro,Natureza natureza);
//	List<Ocorrencia> findByDataRegistroIsBetweenAndNaturezaAndBairro(Date dataInicio, Date dataFim, Natureza natureza,Bairro bairro);
	
	List<Ocorrencia> findByBairroAndNatureza(Bairro bairro,Natureza natureza);

}
