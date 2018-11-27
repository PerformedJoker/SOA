package com.aplicacao.rest.services.exceptions;

public class NaturezaInvalidaException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NaturezaInvalidaException(String mensagem) {
		super(mensagem);
	}
	
	public NaturezaInvalidaException(String mensagem,Throwable causa) {
		super(mensagem,causa);
	}
}
