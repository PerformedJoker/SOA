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
@Table(name="natureza", schema="listas")
public class Natureza implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idNatureza;

	
	private String descricao;
	

	@JsonIgnore
	@OneToMany(mappedBy="natureza", cascade= CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Ocorrencia> listOcorrencias;





	public Integer getIdNatureza() {
		return idNatureza;
	}



	public void setIdNatureza(Integer idNatureza) {
		this.idNatureza = idNatureza;
	}



	public List<Ocorrencia> getListOcorrencias() {
		return listOcorrencias;
	}



	public void setListOcorrencias(List<Ocorrencia> listOcorrencias) {
		this.listOcorrencias = listOcorrencias;
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
		result = prime * result + ((idNatureza == null) ? 0 : idNatureza.hashCode());
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
		Natureza other = (Natureza) obj;
		if (idNatureza == null) {
			if (other.idNatureza != null)
				return false;
		} else if (!idNatureza.equals(other.idNatureza))
			return false;
		return true;
	}



	
	
	
	

	
}
