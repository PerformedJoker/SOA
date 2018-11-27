package com.aplicacao.rest.resources;

import java.net.URI;
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
import com.aplicacao.rest.services.OcorrenciaService;
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
	
	
//	@RequestMapping(method = RequestMethod.POST)
//	public ResponseEntity<?> salvar(@Valid @RequestBody Autor autor){
//		try {
//			autor= autoresService.salvar(autor);
//		} catch (AutorExistenteException e) {
//			DetalheErro detalheErro = new DetalheErro();
//			detalheErro.setStatus("409");
//			detalheErro.setMsgDesenvolvedor("http://errors.localhost.com/");
//			detalheErro.setTitulo("Autor já existente.");
//			return ResponseEntity.status(HttpStatus.CONFLICT).body(detalheErro);
//		}
//		
//		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(autor.getId()).toUri();
//		return ResponseEntity.created(uri).build();
//	}
	
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
}
