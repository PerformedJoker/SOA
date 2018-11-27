package com.aplicacao.rest.resources;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	
	@RequestMapping(value="/data/natureza/{dataInicio}/{dataFim}/{nomeNatureza:.+}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaEntreDatasENatureza( @PathVariable("dataInicio") String dataInicio, @PathVariable("dataFim")String dataFim, @PathVariable("nomeNatureza")String natureza) throws ParseException{
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
	
	@RequestMapping(value="/data/natureza/{dataInicio}/{dataFim}/{idBairro}",method = RequestMethod.GET)
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
	
	@RequestMapping(value="/data/natureza/{dataInicio}/{dataFim}/{nomeBairro}",method = RequestMethod.GET)
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

	@RequestMapping(value="/data/natureza/bairro/{dataInicio}/{dataFim}/{nomeNatureza}/{nomeBairro}",method = RequestMethod.GET)
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
	
	@RequestMapping(value="/natureza/bairro/{nomeNatureza}/{nomeBairro}",method = RequestMethod.GET)
	public ResponseEntity<?> pesquisaNomeBairroENomeNatureza( @PathVariable("nomeBairro")Bairro bairro, @PathVariable("nomeNatureza")Natureza natureza) throws ParseException{
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
	
	@RequestMapping(value="/natureza/{nomeNatureza}",method = RequestMethod.GET)
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
	
	@RequestMapping(value="/bairro/{nomeBairro}",method = RequestMethod.GET)
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
	

}
