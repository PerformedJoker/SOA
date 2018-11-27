package com.aplicacao.rest.domain;

public class DetalheErro {
	private String status;
	private String titulo;
	private String msgDesenvolvedor;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMsgDesenvolvedor() {
		return msgDesenvolvedor;
	}
	public void setMsgDesenvolvedor(String msgDesenvolvedor) {
		this.msgDesenvolvedor = msgDesenvolvedor;
	}
	
	
}
