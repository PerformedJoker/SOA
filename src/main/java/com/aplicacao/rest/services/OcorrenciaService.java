package com.aplicacao.rest.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicacao.rest.domain.Ocorrencia;
import com.aplicacao.rest.repository.OcorrenciaRepository;
import com.aplicacao.rest.services.exceptions.OcorrenciaExistenteException;

@Service
public class OcorrenciaService {
	
	@Autowired	
	private OcorrenciaRepository ocorrenciaRepository;
	
	public List<Ocorrencia> listaOcorrencias(){
		return ocorrenciaRepository.findAll();
	}
	
	public Ocorrencia buscar(Integer id) {
		Ocorrencia ocorrencia = ocorrenciaRepository.findOne(id);
		if(ocorrencia == null) {
			throw new OcorrenciaExistenteException("Esse ocorrencia não existe");
		}
		return ocorrencia;
	}
}
