package com.aplicacao.rest.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicacao.rest.domain.Ocorrencia;
import com.aplicacao.rest.list.Bairro;
import com.aplicacao.rest.list.Natureza;
import com.aplicacao.rest.repository.BairroRepository;
import com.aplicacao.rest.repository.NaturezaRepository;
import com.aplicacao.rest.repository.OcorrenciaRepository;
import com.aplicacao.rest.services.exceptions.BairroInvalidoException;
import com.aplicacao.rest.services.exceptions.NaturezaInvalidaException;
import com.aplicacao.rest.services.exceptions.OcorrenciaDataInvalidaException;
import com.aplicacao.rest.services.exceptions.OcorrenciaExistenteException;

@Service
public class OcorrenciaService {
	
	@Autowired	
	private OcorrenciaRepository ocorrenciaRepository;
	
	@Autowired	
	private NaturezaRepository naturezaRepository;
	
	@Autowired	
	private BairroRepository bairroRepository;
	
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
	
	public List<Ocorrencia> ocorrenciasEntreDatas (Date inicio, Date fim) {
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByDataRegistroIsBetween(inicio, fim);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias nas Datas pesquisadas");
		}
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasEntreDatasENomeNatureza (Date inicio, Date fim, String nomeNatureza) {
		Natureza natureza = naturezaRepository.findByDescricao(nomeNatureza);
		List<Ocorrencia> listaDeOcorrencias = new ArrayList<>();
		if(natureza!=null) {
			listaDeOcorrencias = ocorrenciaRepository.findByDataRegistroIsBetweenAndNatureza(inicio, fim, natureza);
			if(listaDeOcorrencias == null) {
				throw new OcorrenciaExistenteException("Não Existem ocorrencias nas Datas pesquisadas com a Natureza Solicitada");
			}
		}else {
			throw new NaturezaInvalidaException("Esta Natureza não existe");
		}
		
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasEntreDatasENatureza (Date inicio, Date fim, Natureza natureza) {
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByDataRegistroIsBetweenAndNatureza(inicio, fim, natureza);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias nas Datas pesquisadas com a Natureza Solicitada");
		}
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasEntreDatasNaturezaEBairro(Date inicio, Date fim, Natureza natureza,Bairro bairro) {
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByDataRegistroIsBetweenAndBairroAndNatureza(inicio, fim, bairro, natureza);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias nas Datas pesquisadas com a Natureza e Bairro Solicitados");
		}
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasEntreDatasNomeNaturezaENomeBairro(Date inicio, Date fim, String nomeNatureza,String nomeBairro) {
		List<Ocorrencia> listaDeOcorrencias = new ArrayList<>();
		Natureza natureza = naturezaRepository.findByDescricao(nomeNatureza);
		Bairro bairro = bairroRepository.findByDescricao(nomeBairro);
		
		if(natureza==null) {
			throw new NaturezaInvalidaException("Natureza não existe");
		}
		if(bairro==null) {
			throw new NaturezaInvalidaException("Bairro não existe");
		}

		listaDeOcorrencias = ocorrenciaRepository.findByDataRegistroIsBetweenAndBairroAndNatureza(inicio, fim, bairro, natureza);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias nas Datas pesquisadas com a Natureza e Bairro Solicitados");
		}
		return listaDeOcorrencias;
	}
	
	
	public List<Ocorrencia> ocorrenciasEntreDatasEBairro (Date inicio, Date fim,Bairro bairro) {
		Bairro b= bairroRepository.findOne(bairro.getIdBairro());
		if(b==null) {
			throw new NaturezaInvalidaException("Bairro não existe");
		}
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByDataRegistroIsBetweenAndBairro(inicio, fim, bairro);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias nas Datas pesquisadas com a Natureza e Bairro Solicitados");
		}
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasEntreDatasENomeBairro (Date inicio, Date fim,String nomeBairro) {
		List<Ocorrencia> listaDeOcorrencias = new ArrayList<>();
		Bairro bairro = bairroRepository.findByDescricao(nomeBairro);
		if(bairro!=null) {
			listaDeOcorrencias = ocorrenciaRepository.findByDataRegistroIsBetweenAndBairro(inicio, fim, bairro);
			if(listaDeOcorrencias == null) {
				throw new OcorrenciaExistenteException("Não Existem ocorrencias nas Datas pesquisadas com a Natureza e Bairro Solicitados");
			}
		}else {
			throw new BairroInvalidoException("Este Bairro não existe");
		}
		return listaDeOcorrencias;
	}
	
	
	public List<Ocorrencia> ocorrenciasPorNatureza (Natureza natureza) {
		Natureza n = naturezaRepository.findOne(natureza.getIdNatureza());
		if(n==null) {
			throw new NaturezaInvalidaException("Natureza não existe");
		}
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByNatureza(natureza);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias  com a Natureza Solicitados");
		}
		return listaDeOcorrencias;
	}
	

	public List<Ocorrencia> ocorrenciasPorNomeNatureza (String nomeNatureza) {
		Natureza natureza = naturezaRepository.findByDescricao(nomeNatureza);
		if(natureza==null) {
			throw new NaturezaInvalidaException("Natureza não existe");
		}
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByNatureza(natureza);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias  com a Natureza Solicitados");
		}
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasPorBairro (Bairro bairro) {
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByBairro(bairro);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias  com o Bairro Solicitados");
		}
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasPorNomeBairro (String  nomeBairro) {
		Bairro bairro = bairroRepository.findByDescricao(nomeBairro);
		if(bairro==null) {
			throw new NaturezaInvalidaException("Natureza não existe");
		}
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByBairro(bairro);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias  com o Bairro Solicitados");
		}
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasPorNaturezaEBairro (Natureza natureza, Bairro bairro) {
		Natureza n = naturezaRepository.findOne(natureza.getIdNatureza());
		Bairro b= bairroRepository.findOne(bairro.getIdBairro());
		if(n==null) {
			throw new NaturezaInvalidaException("Natureza não existe");
		}
		if(b==null) {
			throw new NaturezaInvalidaException("Bairro não existe");
		}
		
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByBairroAndNatureza(bairro, natureza);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias  com o Bairro Solicitados");
		}
		return listaDeOcorrencias;
	}
	
	public List<Ocorrencia> ocorrenciasPorNomeNaturezaENomeBairro (String nomeNatureza, String nomeBairro) {
		Natureza natureza = naturezaRepository.findByDescricao(nomeNatureza);
		Bairro bairro = bairroRepository.findByDescricao(nomeBairro);
		
		if(natureza==null) {
			throw new NaturezaInvalidaException("Natureza não existe");
		}
		if(bairro==null) {
			throw new BairroInvalidoException("Bairro não existe");
		}
		List<Ocorrencia> listaDeOcorrencias = ocorrenciaRepository.findByBairroAndNatureza(bairro, natureza);
		if(listaDeOcorrencias == null) {
			throw new OcorrenciaExistenteException("Não Existem ocorrencias  com o Bairro Solicitados");
		}
		return listaDeOcorrencias;
	}
	
	
	public String formataData(String data) {
		String resultado =data.substring(0,2)+"/"+data.substring(2,4)+"/"+data.substring(4,8)+" 00:00:00";
		return resultado;
	}
	
	
	public Boolean validaData(String dataInicio,String dataFim) {
		if(dataInicio.isEmpty() && dataFim.isEmpty()) {
			throw new OcorrenciaDataInvalidaException("Datas vazias");
		}else if(dataInicio.isEmpty() && !dataFim.isEmpty()) {
			throw new OcorrenciaDataInvalidaException("Data de Inicio vazia");
		}else if(!dataInicio.isEmpty() && dataFim.isEmpty()) {
			throw new OcorrenciaDataInvalidaException("Data Final vazia");
		}else {
			 if(dataInicio.contains("^[a-Z]") || dataInicio.contains("^[!#@$%¨&*_]") ){
				 throw new OcorrenciaDataInvalidaException("Data de Inicio com formato invalido");
			 }
			 if(dataFim.contains("^[a-Z]") || dataFim.contains("^[!#@$%¨&*_]") ){
				 throw new OcorrenciaDataInvalidaException("Data Final com formato invalido");
			 }
			
			return true;
		}
	}
	
}
