package com.dunedin.questionario;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
//@IdClass(AlternativaPK.class)
@Table(name="PerguntaAlternativa")
@NamedQueries({
    @NamedQuery(name = "Alternativa.findAllPerPerguntaId", query = "SELECT a FROM Alternativa a WHERE a.perguntaId = :perguntaId"),
    @NamedQuery(name = "Alternativa.findAlternativaPerId", query = "SELECT a FROM Alternativa a WHERE a.perguntaId = :perguntaId AND a.id = :alternativaId")
})
public class Alternativa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ALTERNATIVA_PERGUNTA_ID", nullable=false)
	private int id;

	@Id
	@Column(name="PERGUNTA_ID", nullable=false)
	private int perguntaId;

	@Column(name="DESCRICAO", nullable=false)
	private String descricao = "";

	@Column(name="PROLOG", nullable=true)
	private String prolog = "";

	@Column(name="VISIVEL", nullable=false)
	private int visivel = 1;

	public Alternativa(){
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}
	
	public int getPerguntaId(){
		return perguntaId;
	}

	public void setPerguntaId(int perguntaId){
		this.perguntaId = perguntaId;
	}
	
	public String getDescricao(){
		return descricao;
	}

	public void setDescricao(String descricao){
		this.descricao = descricao;
	}
	
	public String getProlog() {
		return prolog;
	}
	
	public void setProlog(String prolog) {
		this.prolog = prolog;
	}
	
	public int getVisivel() {
		return visivel;
	}
	
	public void setVisivel(int visivel) {
		this.visivel = visivel;
	}
	
}
