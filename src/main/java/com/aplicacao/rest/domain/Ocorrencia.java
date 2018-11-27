package com.aplicacao.rest.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.aplicacao.rest.list.Bairro;
import com.aplicacao.rest.list.Natureza;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;





@Entity
public class Ocorrencia implements Serializable,Comparable<Ocorrencia> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idBo;

	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd/MM/yyyy",locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFato;

	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd/MM/yyyy HH:mm:ss",locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern="dd/MM/yyyy HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataRegistro;
	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm",locale = "pt-BR", timezone = "Brazil/East")
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	private Date horaFato;
	

	private String uf;
	
	private String cidade;
	
	@ManyToOne
	@JoinColumn(name = "fk_bairro")
	private Bairro bairro;
	
	@ManyToOne
	@JoinColumn(name = "fk_natureza")
	private Natureza natureza;
	
	
	
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public Natureza getNatureza() {
		return natureza;
	}

	public void setNatureza(Natureza natureza) {
		this.natureza = natureza;
	}

	public Integer getIdBo() {
		return idBo;
	}

	public void setIdBo(Integer idBo) {
		this.idBo = idBo;
	}

	public Date getDataFato() {
		return dataFato;
	}

	public void setDataFato(Date dataFato) {
		this.dataFato = dataFato;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Date getHoraFato() {
		return horaFato;
	}

	public void setHoraFato(Date horaFato) {
		this.horaFato = horaFato;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ocorrencia other = (Ocorrencia) obj;
		if (idBo == null) {
			if (other.idBo != null)
				return false;
		} else if (!idBo.equals(other.idBo))
			return false;
		return true;
	}
	
	public int compareTo(Ocorrencia bo){
	       return this.getIdBo().compareTo(bo.getIdBo());
	}

	public static class comparaIdBo implements Comparator<Ocorrencia>{
        public int compare(Ocorrencia bo1, Ocorrencia bo2){
            int id1 = bo1.getIdBo();
            int id2 = bo2.getIdBo();

            if (id1 == id2)
                return 0;
            else if (id1 > id2)
                return 1;
            else
                return -1;
        }
    }

}

