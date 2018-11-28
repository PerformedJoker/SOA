package com.aplicacao.rest.resources;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aplicacao.rest.domain.DetalheErro;
import com.aplicacao.rest.domain.Ocorrencia;
import com.aplicacao.rest.list.Bairro;
import com.aplicacao.rest.list.Natureza;
import com.aplicacao.rest.services.OcorrenciaService;
import com.aplicacao.rest.services.exceptions.BairroInvalidoException;
import com.aplicacao.rest.services.exceptions.NaturezaInvalidaException;
import com.aplicacao.rest.services.exceptions.OcorrenciaDataInvalidaException;
import com.aplicacao.rest.services.exceptions.OcorrenciaExistenteException;

@RestController
@RequestMapping(value="/ocorrencia")
public class OcorrenciasResources {
	
	
	@Autowired
	private OcorrenciaService ocorrenciaService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Ocorrencia>> listar(){
		return ResponseEntity.status(HttpStatus.OK).body(ocorrenciaService.listaOcorrencias());
	}
	
	@RequestMapping(value="/quantidade",method = RequestMethod.GET)
	public  ResponseEntity<?> quantidadeOcorrencias(){
		return ResponseEntity.status(HttpStatus.OK).body(ocorrenciaService.listaOcorrencias().size());
	}
	
	@RequestMapping(value="/ranking/naturezas",method = RequestMethod.GET)
	public  ResponseEntity<?> rankingOcorrenciasNaturezas(){
		 HashMap<Integer,String> ranking = ocorrenciaService.rankingTodasOcorrenciasPorNatureza();
		return ResponseEntity.status(HttpStatus.OK).body(ranking);
	}
	
	@RequestMapping(value="/ranking/bairros",method = RequestMethod.GET)
	public  ResponseEntity<?> rankingOcorrenciasBairros(){
		 HashMap<Integer,String> ranking = ocorrenciaService.rankingTodasOcorrenciasPorBairro();
		return ResponseEntity.status(HttpStatus.OK).body(ranking);
	}
	
	
	@RequestMapping(value="/{id}",method = RequestMethod.GET)
	public ResponseEntity<?> buscar( @PathVariable("id") Integer id){
		Ocorrencia autor;
		try {
			autor = ocorrenciaService.buscar(id);
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(autor);
	}
	
	
	@RequestMapping(value="/data/{dataInicio}/{dataFim}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatas( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatas(datesIn, datesFi);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/data/ranking/natureza/{dataInicio}/{dataFim}",method = RequestMethod.GET)
	public  ResponseEntity<?> rankingOcorrenciasPorDatasENatureza(@PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim) throws ParseException{
		HashMap<Integer,String> ranking= new HashMap<Integer,String>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
		
		
		 try {
				
				if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
					dataInicio = ocorrenciaService.formataData(dataInicio);
					dataFim = ocorrenciaService.formataData(dataFim);
					Date dateI = formatter1.parse(dataInicio);
					Date dateF = formatter1.parse(dataFim);
					Date datesIn = (Date) dateI;
					Date datesFi= (Date) dateF;
					ranking = ocorrenciaService.rankingTodasOcorrenciasPorDatasNatureza(datesIn, datesFi);
				}
			
				
				
			} catch (OcorrenciaExistenteException e) {
				DetalheErro detalheErro = new DetalheErro();
				detalheErro.setStatus("404");
				detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
				detalheErro.setTitulo("Ocorrencia não encontrada.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
			}catch (OcorrenciaDataInvalidaException e) {
				DetalheErro detalheErro = new DetalheErro();
				detalheErro.setStatus("404");
				detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
				detalheErro.setTitulo("Data Invalida.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(ranking);
	}
	
	@RequestMapping(value="/data/ranking/bairro/{dataInicio}/{dataFim}",method = RequestMethod.GET)
	public  ResponseEntity<?> rankingOcorrenciasPorDatasEBairro(@PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim) throws ParseException{
		HashMap<Integer,String> ranking= new HashMap<Integer,String>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
		
		
		 try {
				
				if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
					dataInicio = ocorrenciaService.formataData(dataInicio);
					dataFim = ocorrenciaService.formataData(dataFim);
					Date dateI = formatter1.parse(dataInicio);
					Date dateF = formatter1.parse(dataFim);
					Date datesIn = (Date) dateI;
					Date datesFi= (Date) dateF;
					ranking = ocorrenciaService.rankingTodasOcorrenciasPorDatasBairro(datesIn, datesFi);
				}
			
				
				
			} catch (OcorrenciaExistenteException e) {
				DetalheErro detalheErro = new DetalheErro();
				detalheErro.setStatus("404");
				detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
				detalheErro.setTitulo("Ocorrencia não encontrada.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
			}catch (OcorrenciaDataInvalidaException e) {
				DetalheErro detalheErro = new DetalheErro();
				detalheErro.setStatus("404");
				detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
				detalheErro.setTitulo("Data Invalida.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(ranking);
	}
	
	@RequestMapping(value="/data/quantidade/{dataInicio}/{dataFim}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasContador( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatas(datesIn, datesFi);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	
	@RequestMapping(value="/data/natureza/{dataInicio}/{dataFim}/{idNatureza}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENatureza( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("idNatureza")Natureza natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasENatureza(datesIn, datesFi,natureza);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/data/natureza/quantidade/{dataInicio}/{dataFim}/{idNatureza}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENaturezaContador( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("idNatureza")Natureza natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasENatureza(datesIn, datesFi,natureza);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/data/natureza/nome/{dataInicio}/{dataFim}/{nomeNatureza}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENomeNatureza( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("nomeNatureza")String natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				natureza = natureza.toUpperCase();
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasENomeNatureza(datesIn, datesFi, natureza);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/data/natureza/nome/quantidade/{dataInicio}/{dataFim}/{nomeNatureza}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENomeNaturezaContador( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("nomeNatureza")String natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				natureza = natureza.toUpperCase();
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasENomeNatureza(datesIn, datesFi, natureza);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/data/bairro/{dataInicio}/{dataFim}/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasEBairro( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("idBairro")Bairro bairro) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasEBairro(datesIn, datesFi,bairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/data/bairro/quantidade/{dataInicio}/{dataFim}/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasEBairroContador( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("idBairro")Bairro bairro) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasEBairro(datesIn, datesFi,bairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/data/bairro/ranking/{dataInicio}/{dataFim}/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasEBairroRanking( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("idBairro")Bairro bairro) throws ParseException{
		HashMap<Integer,String> ranking = new HashMap<Integer,String>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ranking = ocorrenciaService.rankingTodasOcorrenciasPorDatasEBairroId(datesIn, datesFi,bairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ranking);
	}
	
	@RequestMapping(value="/data/bairro/nome/{dataInicio}/{dataFim}/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENomeBairro( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("nomeBairro")String nomeBairro) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				nomeBairro = nomeBairro.toUpperCase();
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasENomeBairro(datesIn, datesFi,nomeBairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/data/bairro/nome/quantidade/{dataInicio}/{dataFim}/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENomeBairroContador( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("nomeBairro")String nomeBairro) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				nomeBairro = nomeBairro.toUpperCase();
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasENomeBairro(datesIn, datesFi,nomeBairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/data/bairro/nome/ranking/{dataInicio}/{dataFim}/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENomeBairroRanking( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("nomeBairro")String nomeBairro) throws ParseException{
		HashMap<Integer,String> ranking = new HashMap<Integer,String>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				nomeBairro = nomeBairro.toUpperCase();
				ranking = ocorrenciaService.rankingTodasOcorrenciasPorDatasEBairroNome(datesIn, datesFi,nomeBairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ranking);
	}
	
	
	@RequestMapping(value="/data/natureza/bairro/{dataInicio}/{dataFim}/{idNatureza}/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasEBairroENatureza( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("idBairro")Bairro bairro, @PathVariable("idNatureza")Natureza natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasNaturezaEBairro(datesIn, datesFi,natureza,bairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	@RequestMapping(value="/data/natureza/bairro/quantidade/{dataInicio}/{dataFim}/{idNatureza}/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasEBairroENaturezaContador( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("idBairro")Bairro bairro, @PathVariable("idNatureza")Natureza natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasNaturezaEBairro(datesIn, datesFi,natureza,bairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}

	@RequestMapping(value="/data/natureza/bairro/nome/{dataInicio}/{dataFim}/{nomeNatureza}/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENomeBairroENomeNatureza(@PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("nomeBairro")String nomebairro, @PathVariable("nomeNatureza")String nomeNatureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				nomebairro = nomebairro.toUpperCase();
				nomeNatureza = nomeNatureza.toUpperCase();
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasNomeNaturezaENomeBairro(datesIn, datesFi,nomeNatureza,nomebairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/data/natureza/bairro/nome/quantidade/{dataInicio}/{dataFim}/{nomeNatureza}/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENomeBairroENomeNaturezaContador(@PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("nomeBairro")String nomebairro, @PathVariable("nomeNatureza")String nomeNatureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			
			if(ocorrenciaService.validaData(dataInicio,dataFim)== true) {
				dataInicio = ocorrenciaService.formataData(dataInicio);
				dataFim = ocorrenciaService.formataData(dataFim);
				Date dateI = formatter1.parse(dataInicio);
				Date dateF = formatter1.parse(dataFim);
				Date datesIn = (Date) dateI;
				Date datesFi= (Date) dateF;
				nomebairro = nomebairro.toUpperCase();
				nomeNatureza = nomeNatureza.toUpperCase();
				ocorrencias = ocorrenciaService.ocorrenciasEntreDatasNomeNaturezaENomeBairro(datesIn, datesFi,nomeNatureza,nomebairro);
			}
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (OcorrenciaDataInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Data Invalida.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/natureza/bairro/{idNatureza}/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroENatureza( @PathVariable("idBairro")Bairro bairro, @PathVariable("idNatureza")Natureza natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorNaturezaEBairro(natureza,bairro);
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/natureza/bairro/quantidade/{idNatureza}/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroENaturezaContador( @PathVariable("idBairro")Bairro bairro, @PathVariable("idNatureza")Natureza natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorNaturezaEBairro(natureza,bairro);
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/natureza/bairro/nome/{nomeNatureza}/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaNomeBairroENomeNatureza( @PathVariable("nomeBairro")String bairro, @PathVariable("nomeNatureza")String natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			bairro = bairro.toUpperCase();
			natureza = natureza.toUpperCase();
				ocorrencias = ocorrenciaService.ocorrenciasPorNomeNaturezaENomeBairro(natureza,bairro);
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrado.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/natureza/bairro/nome/quantidade/{nomeNatureza}/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaNomeBairroENomeNaturezaContador( @PathVariable("nomeBairro")String bairro, @PathVariable("nomeNatureza")String natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
			bairro = bairro.toUpperCase();
			natureza = natureza.toUpperCase();
				ocorrencias = ocorrenciaService.ocorrenciasPorNomeNaturezaENomeBairro(natureza,bairro);
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrado.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	
	@RequestMapping(value="/natureza/{idNatureza}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroENatureza(@PathVariable("idNatureza")Natureza natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorNatureza(natureza);
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/natureza/quantidade/{idNatureza}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroENaturezaContador(@PathVariable("idNatureza")Natureza natureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorNatureza(natureza);
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/natureza/nome/{nomeNatureza}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroENomeNatureza(@PathVariable("idNatureza")String nomeNatureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorNomeNatureza(nomeNatureza);
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/natureza/nome/quantidade/{nomeNatureza}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroENomeNaturezaContador(@PathVariable("idNatureza")String nomeNatureza) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorNomeNatureza(nomeNatureza);
		
			
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (NaturezaInvalidaException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Natureza não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/bairro/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroEBairro(@PathVariable("idBairro")Bairro bairro) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorBairro(bairro);
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/bairro/quantidade/{idBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroEBairroContador(@PathVariable("idBairro")Bairro bairro) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorBairro(bairro);
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	
	@RequestMapping(value="/bairro/nome/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroENomeBairro(@PathVariable("nomeBairro")String nomeBairro) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorNomeBairro(nomeBairro);
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias);
	}
	
	@RequestMapping(value="/bairro/nome/quantidade/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaBairroENomeBairroContador(@PathVariable("nomeBairro")String nomeBairro) throws ParseException{
		List<Ocorrencia> ocorrencias = new ArrayList<>();
		SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy 00:00:00");
	
		try {
				ocorrencias = ocorrenciaService.ocorrenciasPorNomeBairro(nomeBairro);
			
		} catch (OcorrenciaExistenteException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Ocorrencia não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}catch (BairroInvalidoException e) {
			DetalheErro detalheErro = new DetalheErro();
			detalheErro.setStatus("404");
			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
			detalheErro.setTitulo("Bairro não encontrada.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detalheErro);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ocorrencias.size());
	}
	

}
