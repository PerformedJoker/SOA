package com.aplicacao.rest.list;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.aplicacao.rest.domain.Ocorrencia;
import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name="bairro", schema="public")
public class Bairro implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idBairro;

	
	private String descricao;
	

	@JsonIgnore
	@OneToMany(mappedBy="bairro", cascade= CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Ocorrencia> listOcorrencias;


	public Integer getIdBairro() {
		return idBairro;
	}



	public void setIdBairro(Integer idBairro) {
		this.idBairro = idBairro;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idBairro == null) ? 0 : idBairro.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bairro other = (Bairro) obj;
		if (idBairro == null) {
			if (other.idBairro != null)
				return false;
		} else if (!idBairro.equals(other.idBairro))
			return false;
		return true;
	}


	
}
