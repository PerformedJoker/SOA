package com.aplicacao.rest.services.exceptions;

public class OcorrenciaDataInvalidaException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OcorrenciaDataInvalidaException(String mensagem) {
		super(mensagem);
	}
	
	public OcorrenciaDataInvalidaException(String mensagem,Throwable causa) {
		super(mensagem,causa);
	}
}
