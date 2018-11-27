package com.aplicacao.rest.services.exceptions;

public class OcorrenciaExistenteException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OcorrenciaExistenteException(String mensagem) {
		super(mensagem);
	}
	
	public OcorrenciaExistenteException(String mensagem,Throwable causa) {
		super(mensagem,causa);
	}
}
