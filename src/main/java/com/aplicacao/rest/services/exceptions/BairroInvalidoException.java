package com.aplicacao.rest.services.exceptions;

public class BairroInvalidoException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BairroInvalidoException(String mensagem) {
		super(mensagem);
	}
	
	public BairroInvalidoException(String mensagem,Throwable causa) {
		super(mensagem,causa);
	}
}
