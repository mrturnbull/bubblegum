package com.dunedin.questionario;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="PerguntaEnunciado")
@NamedQueries({
    @NamedQuery(name = "PerguntaEnunciado.findPerId",   query = "SELECT pe FROM Pergunta pe WHERE pe.id = :id"),
    @NamedQuery(name = "PerguntaEnunciado.findAll",    query = "SELECT pe FROM Pergunta pe ORDER BY pe.id")
})
public class Pergunta implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PERGUNTAENUNCIADO_ID", nullable=false)
	private int id;

	@Column(name="ENUNCIADO", nullable=false)
	private String enunciado;

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	@JoinColumn(name="PERGUNTA_ID")
	private List<Alternativa> alternativas;
	
	@Column(name="TIPO", nullable=false)
	private int tipo;

	@Column(name="ORDEM", nullable=true)
	private int ordem;

	@Column(name="VISIVEL", nullable=false)
	private int visivel = 1;

	public Pergunta(){}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public String getEnunciado(){
		return enunciado;
	}

	public void setEnunciado(String enunciado){
		this.enunciado = enunciado;
	}

	public List<Alternativa> getAlternativas(){
		return alternativas;
	}
	
	public void setAlternativas(List<Alternativa> alternativas){
		this.alternativas = alternativas;
	}
	
	public int getTipo(){
		return tipo;
	}

	public void setTipo(int tipo){
		this.tipo = tipo;
	}

	public int getOrdem(){
		return ordem;
	}

	public void setOrdem(int ordem){
		this.ordem = ordem;
	}

	public int getVisivel(){
		return visivel;
	}

	public void setVisivel(int visivel){
		this.visivel = visivel;
	}

}
