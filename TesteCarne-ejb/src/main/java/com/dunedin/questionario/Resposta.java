package com.dunedin.questionario;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="Usuario_Resposta")
public class Resposta implements Serializable{

  private static final long serialVersionUID = 1L;

  @Id	
  @Column(name="USUARIO_ID", nullable=false)
  private long usuarioId;

  @Column(name="PERGUNTA_ID", nullable=false)
  private int  perguntaId;

  @Column(name="RESPOSTA_ID", nullable=false)
  private int  respostaId;

  @Column(name="DATA_INICIO", nullable=true)
	private String dataInicio;

  @Column(name="DATA_FIM", nullable=true)
  private String dataFim;

  public Resposta(){
  }

	public long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getPerguntaId() {
		return perguntaId;
	}

	public void setPerguntaId(int perguntaId) {
		this.perguntaId = perguntaId;
	}

	public int getRespostaId() {
		return respostaId;
	}

	public void setRespostaId(int respostaId) {
		this.respostaId = respostaId;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

}
