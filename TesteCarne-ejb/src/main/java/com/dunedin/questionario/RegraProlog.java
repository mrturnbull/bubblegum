package com.dunedin.questionario;

import java.io.Serializable;
//import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RegraProlog")
public class RegraProlog implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="REGRAPROLOG_ID")
	private long id;

	@Column(name="PALAVRA", nullable=false)
	private String palavra;

	public RegraProlog(){}

	public long getId(){
		return id;
	}

	public void setId(long id){
		this.id = id;
	}

	public String getPalavra(){
		return palavra;
	}

	public void setPalavra(String palavra){
		this.palavra = palavra;
	}

}

